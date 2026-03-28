package org.yorku.auctionmanager.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.yorku.auctionmanager.service.AuctionDatabaseManager;
import org.yorku.auctionmanager.repository.SqliteAuctionDAO;
import org.yorku.auctionmanager.dto.*;
import org.yorku.auctionmanager.model.Item;
import org.yorku.auctionmanager.model.AuctionUpdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuctionDatabaseManagerImpl implements AuctionDatabaseManager {

    private final SqliteAuctionDAO auctionDAO;
    private final RestTemplate restTemplate;
    
    // Updated to port 8083 based on your Payment Service application.properties
    private final String PAYMENT_SERVICE_URL = "http://localhost:8083/api/payment/register-pending";

    public AuctionDatabaseManagerImpl(SqliteAuctionDAO auctionDAO) {
        this.auctionDAO = auctionDAO;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public AuctionDatabaseResponse fetchCatalogue() {
        try {
            List<Item> catalogue = auctionDAO.fetchAllItems();
            return new AuctionDatabaseResponse(0, "Success", catalogue);
        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Database Error: " + e.getMessage(), new ArrayList<>());
        }
    }

    @Override
    public AuctionDatabaseResponse addItem(AuthenticatedRequest request) {
        try {
            ItemRequest itemPayload = (ItemRequest) request.getRequest();
            Item newItem = itemPayload.getItem();
            
            if (newItem.getName() == null || newItem.getName().trim().isEmpty() || newItem.getStartingPrice() < 0) {
                return new AuctionDatabaseResponse(10, "Invalid Item Data", new ArrayList<>());
            }

            newItem.setOwnerUid(request.getAccountUID());
            boolean isSaved = auctionDAO.insertItem(newItem);
            
            if (isSaved) {
                return new AuctionDatabaseResponse(0, "Item added successfully", Collections.singletonList(newItem));
            } else {
                return new AuctionDatabaseResponse(2, "Failed to write to database", new ArrayList<>());
            }
        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Internal Server Error", new ArrayList<>());
        }
    }

    @Override
    public AuctionDatabaseResponse removeItem(AuthenticatedRequest request) {
        try {
            ItemIdRequest idPayload = (ItemIdRequest) request.getRequest();
            int targetId = idPayload.getItemId();
            Item targetItem = auctionDAO.fetchItemById(targetId);

            if (targetItem == null) return new AuctionDatabaseResponse(11, "Not found", new ArrayList<>());
            if (targetItem.getOwnerUid() != request.getAccountUID()) return new AuctionDatabaseResponse(16, "Denied", new ArrayList<>());

            boolean isDeleted = auctionDAO.deleteItem(targetId);
            return isDeleted ? new AuctionDatabaseResponse(0, "Removed", new ArrayList<>()) : new AuctionDatabaseResponse(2, "DB Error", new ArrayList<>());
        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Error", new ArrayList<>());
        }
    }

    @Override
    public AuctionDatabaseResponse modifyItem(AuthenticatedRequest request) {
        try {
            ItemRequest itemPayload = (ItemRequest) request.getRequest();
            Item modifiedItemData = itemPayload.getItem();
            Item existingItem = auctionDAO.fetchItemById(modifiedItemData.getId());

            if (existingItem == null) return new AuctionDatabaseResponse(11, "Not found", new ArrayList<>());
            if (existingItem.getOwnerUid() != request.getAccountUID()) return new AuctionDatabaseResponse(16, "Denied", new ArrayList<>());

            boolean wasOpen = !existingItem.isClosed();
            
            existingItem.setName(modifiedItemData.getName());
            existingItem.setDescription(modifiedItemData.getDescription());
            existingItem.setCurrentHighestBid(modifiedItemData.getCurrentHighestBid());
            existingItem.setHighestBidderUid(modifiedItemData.getHighestBidderUid());
            existingItem.setClosed(modifiedItemData.isClosed()); 

            boolean isUpdated = auctionDAO.updateItem(existingItem);
            
            if (isUpdated) {
                // Check if the auction just closed and has a winner
                if (existingItem.isClosed() && wasOpen && existingItem.getHighestBidderUid() > 0) {
                    notifyPaymentService(existingItem);
                }
                return new AuctionDatabaseResponse(0, "Modified successfully", Collections.singletonList(existingItem));
            }
            return new AuctionDatabaseResponse(2, "Update failed", new ArrayList<>());
        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Internal Error: " + e.getMessage(), new ArrayList<>());
        }
    }

    @Override
    public AuctionDatabaseResponse fetchUserItems(AuthenticatedRequest request) {
        try {
            List<Item> myItems = auctionDAO.fetchItemsByOwner(request.getAccountUID());
            return new AuctionDatabaseResponse(0, "Success", myItems);
        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Error fetching user items", new ArrayList<>());
        }
    }

    @Override
    public AuctionDatabaseResponse pushAuctionUpdate(AuctionUpdate auctionUpdate) {
        try {
            Item item = auctionDAO.fetchItemById(auctionUpdate.getItemId());
            if (item == null) return new AuctionDatabaseResponse(2, "Item not found", new ArrayList<>());

            boolean wasOpen = !item.isClosed();
            item.setCurrentHighestBid(auctionUpdate.getNewBidAmount());
            item.setHighestBidderUid(auctionUpdate.getNewHighestBidderUid());
            item.setClosed(auctionUpdate.isAuctionEnding());

            if (auctionDAO.updateItem(item)) {
                // Register pending payment if auction ended with a winner
                if (item.isClosed() && wasOpen && item.getHighestBidderUid() > 0) {
                    notifyPaymentService(item);
                }
                return new AuctionDatabaseResponse(0, "Auction updated", new ArrayList<>());
            }
            return new AuctionDatabaseResponse(2, "Update failed", new ArrayList<>());
        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "System Error", new ArrayList<>());
        }
    }

    /**
     * Helper method updated to accept an Item object and perform the 
     * cross-microservice REST call to the Payment Service.
     */
    private void notifyPaymentService(Item item) {
        try {
            // 1. Create the inner PayRequest data
            java.util.Map<String, Object> payRequestData = new java.util.HashMap<>();
            payRequestData.put("requestType", "PayRequest"); // Matches @JsonSubTypes name
            payRequestData.put("accountUID", item.getHighestBidderUid());
            payRequestData.put("itemId", item.getId());

            // 2. Wrap it in the AuthenticatedRequest structure
            java.util.Map<String, Object> wrapper = new java.util.HashMap<>();
            wrapper.put("request", payRequestData);
            // We leave 'secret' out or set it to null; Jackson will handle it fine now
            
            // 3. Send to Port 8083
            restTemplate.postForObject(PAYMENT_SERVICE_URL, wrapper, Object.class);
            
            System.out.println("SUCCESS: Pending payment registered for Item " + item.getId());
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            // This will now print the ACTUAL reason if it still fails
            System.err.println("Payment Service Error (" + e.getStatusCode() + "): " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Connection Error: " + e.getMessage());
        }
    }
}
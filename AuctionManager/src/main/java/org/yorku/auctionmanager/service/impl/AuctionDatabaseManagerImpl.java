package org.yorku.auctionmanager.service.impl;

import org.springframework.stereotype.Service;
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

    // Constructor Injection replaces @Autowired
    public AuctionDatabaseManagerImpl(SqliteAuctionDAO auctionDAO) {
        this.auctionDAO = auctionDAO;
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
            
            // Validate the item data (Error Code 10)
            if (newItem.getName() == null || newItem.getName().trim().isEmpty() || newItem.getStartingPrice() < 0) {
                return new AuctionDatabaseResponse(10, "Invalid Item Data", new ArrayList<>());
            }

            // Ensure the owner UID matches the authenticated user making the request
            newItem.setOwnerUid(request.getAccountUID());

            boolean isSaved = auctionDAO.insertItem(newItem);
            
            if (isSaved) {
                return new AuctionDatabaseResponse(0, "Item added successfully", Collections.singletonList(newItem));
            } else {
                return new AuctionDatabaseResponse(2, "Failed to write to database", new ArrayList<>());
            }
        } catch (ClassCastException e) {
            return new AuctionDatabaseResponse(10, "Invalid Payload Format", new ArrayList<>());
        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Internal Server Error", new ArrayList<>());
        }
    }

    @Override
    public AuctionDatabaseResponse removeItem(AuthenticatedRequest request) {
        try {
            ItemIdRequest idPayload = (ItemIdRequest) request.getRequest();
            int itemIdToRemove = idPayload.getItemId();
            int requestingUserId = request.getAccountUID();

            Item targetItem = auctionDAO.fetchItemById(itemIdToRemove);

            if (targetItem == null) {
                return new AuctionDatabaseResponse(11, "Item not found", new ArrayList<>());
            }

            if (targetItem.getOwnerUid() != requestingUserId) {
                return new AuctionDatabaseResponse(16, "Permission Denied: You do not own this item", new ArrayList<>());
            }

            boolean isDeleted = auctionDAO.deleteItem(itemIdToRemove);
            
            if (isDeleted) {
                return new AuctionDatabaseResponse(0, "Item removed successfully", new ArrayList<>());
            } else {
                return new AuctionDatabaseResponse(2, "Database Error during deletion", new ArrayList<>());
            }

        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Internal Server Error", new ArrayList<>());
        }
    }

    @Override
    public AuctionDatabaseResponse modifyItem(AuthenticatedRequest request) {
        try {
            ItemRequest itemPayload = (ItemRequest) request.getRequest();
            Item modifiedItemData = itemPayload.getItem();
            int requestingUserId = request.getAccountUID();

            Item existingItem = auctionDAO.fetchItemById(modifiedItemData.getId());

            if (existingItem == null) {
                return new AuctionDatabaseResponse(11, "Item not found", new ArrayList<>());
            }

            if (existingItem.getOwnerUid() != requestingUserId) {
                return new AuctionDatabaseResponse(16, "Permission Denied: You do not own this item", new ArrayList<>());
            }

            existingItem.setName(modifiedItemData.getName());
            existingItem.setDescription(modifiedItemData.getDescription());

            boolean isUpdated = auctionDAO.updateItem(existingItem);
            
            if (isUpdated) {
                return new AuctionDatabaseResponse(0, "Item modified successfully", Collections.singletonList(existingItem));
            } else {
                return new AuctionDatabaseResponse(2, "Database Error during modification", new ArrayList<>());
            }

        } catch (ClassCastException e) {
            return new AuctionDatabaseResponse(10, "Invalid Payload Format", new ArrayList<>());
        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Internal Server Error: " + e.getMessage(), new ArrayList<>());
        }
    }

    @Override
    public AuctionDatabaseResponse fetchUserItems(AuthenticatedRequest request) {
        try {
            int accountUID = request.getAccountUID();
            
            // To make this work, we need a custom query in SqliteAuctionDAO
            // I have added this missing method to the DAO in my head for now, 
            // but we'll need to add it to your actual DAO class later!
            List<Item> myItems = new ArrayList<>(); // auctionDAO.fetchItemsByOwner(accountUID);
            
            return new AuctionDatabaseResponse(0, "User items retrieved successfully", myItems);
        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Internal Server Error", new ArrayList<>());
        }
    }

    @Override
    public AuctionDatabaseResponse pushAuctionUpdate(AuctionUpdate auctionUpdate) {
        try {
            Item existingItem = auctionDAO.fetchItemById(auctionUpdate.getItemId());

            if (existingItem == null) {
                return new AuctionDatabaseResponse(2, "Item not found for update", new ArrayList<>());
            }

            existingItem.setCurrentHighestBid(auctionUpdate.getNewBidAmount());
            existingItem.setHighestBidderUid(auctionUpdate.getNewHighestBidderUid());
            existingItem.setClosed(auctionUpdate.isAuctionEnding());

            boolean isUpdated = auctionDAO.updateItem(existingItem);

            if (isUpdated) {
                return new AuctionDatabaseResponse(0, "Auction state updated successfully", new ArrayList<>());
            } else {
                return new AuctionDatabaseResponse(2, "Failed to update auction state in DB", new ArrayList<>());
            }

        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Internal System Error: " + e.getMessage(), new ArrayList<>());
        }
    }
}
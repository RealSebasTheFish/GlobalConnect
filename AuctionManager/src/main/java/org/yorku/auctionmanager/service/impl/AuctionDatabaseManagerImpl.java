package org.yorku.auctionmanager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yorku.auctionmanager.service.AuctionDatabaseManager;
import org.yorku.auctionmanager.repository.AuctionDAO;
import org.yorku.auctionmanager.dto.AuthenticatedRequest;
import org.yorku.auctionmanager.dto.AuctionDatabaseResponse;
import org.yorku.auctionmanager.model.Item;
import org.yorku.auctionmanager.model.AuctionUpdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuctionDatabaseManagerImpl implements AuctionDatabaseManager {

    @Autowired
    private AuctionDAO auctionDAO;

    @Override
    public AuctionDatabaseResponse fetchCatalogue() {
        try {
            List<Item> catalogue = auctionDAO.fetchAllItems();
            return new AuctionDatabaseResponse(0, "Success", catalogue);
        } catch (Exception e) {
            // Error code 2: Internal Server Error
            return new AuctionDatabaseResponse(2, "Database Error: " + e.getMessage(), new ArrayList<>());
        }
    }

    @Override
    public AuctionDatabaseResponse addItem(AuthenticatedRequest request) {
        try {
            // Extract the item from the generic payload
            Item newItem = (Item) request.getRequestPayload();
            
            // Validate the item data (Error Code 10)
            if (newItem.getName() == null || newItem.getName().trim().isEmpty() || newItem.getStartingPrice() < 0) {
                return new AuctionDatabaseResponse(10, "Invalid Item Data", new ArrayList<>());
            }

            // Ensure the owner UID matches the authenticated user making the request
            newItem.setOwnerUid(request.getAccountUID());

            Item savedItem = auctionDAO.insertItem(newItem);
            
            if (savedItem != null) {
                // Return a list of length 1 containing the new item
                return new AuctionDatabaseResponse(0, "Item added successfully", Collections.singletonList(savedItem));
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
            // Assuming the payload for a remove request is just the Integer ID of the item
            Integer itemIdToRemove = (Integer) request.getRequestPayload();
            int requestingUserId = request.getAccountUID();

            // Note: To perfectly distinguish between Error 11 (Not Found) and Error 16 (Permission Denied),
            // you would ideally fetch the item first to check its owner.
            // Item targetItem = auctionDAO.fetchItemById(itemIdToRemove);
            // if (targetItem == null) return new AuctionDatabaseResponse(11, "Item not found", emptyList);
            // if (targetItem.getOwnerUid() != requestingUserId) return new AuctionDatabaseResponse(16, "Permission Denied", emptyList);

            boolean isDeleted = auctionDAO.deleteItem(itemIdToRemove, requestingUserId);
            
            if (isDeleted) {
                return new AuctionDatabaseResponse(0, "Item removed successfully", new ArrayList<>());
            } else {
                // If the delete failed, it's either not found or the user doesn't own it
                return new AuctionDatabaseResponse(11, "Item not found or Permission Denied", new ArrayList<>());
            }
        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Internal Server Error", new ArrayList<>());
        }
    }

    @Override
    public AuctionDatabaseResponse modifyItem(AuthenticatedRequest request) {
        // Similar to addItem and removeItem combined:
        // 1. Extract Item from payload.
        // 2. Fetch existing item from DB (check if exists -> Error 11).
        // 3. Check if existingItem.getOwnerUid() == request.getAccountUID() (check permission -> Error 16).
        // 4. Update in DB and return list of length 1.
        return new AuctionDatabaseResponse(0, "Stubbed method", new ArrayList<>());
    }

    @Override
    public AuctionDatabaseResponse fetchUserItems(AuthenticatedRequest request) {
        // 1. Get the accountUID from the request.
        // 2. Query the DB for items where owner_uid == accountUID.
        // 3. Return the list.
        return new AuctionDatabaseResponse(0, "Stubbed method", new ArrayList<>());
    }

    @Override
    public AuctionDatabaseResponse pushAuctionUpdate(AuctionUpdate auctionUpdate) {
        try {
            // 1. Extract data from the auctionUpdate model
            // 2. Update the item's current highest bid and highest bidder in the database
            // auctionDAO.updateItemBids(auctionUpdate.getItemId(), auctionUpdate.getNewBidAmount(), ...);
            
            return new AuctionDatabaseResponse(0, "Auction state updated", new ArrayList<>());
        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Internal System Error", new ArrayList<>());
        }
    }
}
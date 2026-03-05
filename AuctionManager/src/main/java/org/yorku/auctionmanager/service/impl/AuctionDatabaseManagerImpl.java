package org.yorku.auctionmanager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yorku.auctionmanager.service.AuctionDatabaseManager;
import org.yorku.auctionmanager.repository.AuctionDAO;
import org.yorku.auctionmanager.dto.*;
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
        	// Cast to the specific ItemRequest
            ItemRequest itemPayload = (ItemRequest) request.getRequest();
            // Extract the Item from the payload
            Item newItem = itemPayload.getItem();
            
            int accountUID = request.getAccountUID();
            
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
        	// Cast to the specific ItemIdRequest
            ItemIdRequest idPayload = (ItemIdRequest) request.getRequest();
            // Extract the ID
            int itemIdToRemove = idPayload.getItemId();
            
            int requestingUserId = request.getAccountUID();

            // 1. Fetch the item first (fetchItemById in the DAO!)
            Item targetItem = auctionDAO.fetchItemById(itemIdToRemove);

            // 2. Check for Error 11: Item Not Found
            if (targetItem == null) {
                return new AuctionDatabaseResponse(11, "Item not found", new ArrayList<>());
            }

            // 3. Check for Error 16: Permission Denied
            if (targetItem.getOwnerUid() != requestingUserId) {
                return new AuctionDatabaseResponse(16, "Permission Denied: You do not own this item", new ArrayList<>());
            }

            // 4. If it exists and they own it, perform the delete!
            boolean isDeleted = auctionDAO.deleteItem(itemIdToRemove, requestingUserId);
            
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
        	// Cast to the specific ItemRequest
            ItemRequest itemPayload = (ItemRequest) request.getRequest();
            // Extract the Item from the payload
            Item modifiedItemData = itemPayload.getItem();
            int requestingUserId = request.getAccountUID();

            // 2. Fetch the current item from the database
            Item existingItem = auctionDAO.fetchItemById(modifiedItemData.getId());

            // 3. Check for Error 11: Item Not Found
            if (existingItem == null) {
                return new AuctionDatabaseResponse(11, "Item not found", new ArrayList<>());
            }

            // 4. Check for Error 16: Permission Denied (Only the owner can modify)
            if (existingItem.getOwnerUid() != requestingUserId) {
                return new AuctionDatabaseResponse(16, "Permission Denied: You do not own this item", new ArrayList<>());
            }

            // 5. Apply the allowed modifications (e.g., updating name and description)
            existingItem.setName(modifiedItemData.getName());
            existingItem.setDescription(modifiedItemData.getDescription());
            // Note: We intentionally don't overwrite current bids or bidder IDs here!

            // 6. Save the updated item back to the database
            boolean isUpdated = auctionDAO.updateItem(existingItem);
            
            if (isUpdated) {
                // Return a list of length 1 with the modified item as required by your SDD
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
        // 1. Get the accountUID from the request.
        // 2. Query the DB for items where owner_uid == accountUID.
        // 3. Return the list.
    	try {
            int accountUID = request.getAccountUID();
            
            // Use DAO method!
            List<Item> myItems = auctionDAO.fetchItemsByOwner(accountUID);
            
            return new AuctionDatabaseResponse(0, "User items retrieved successfully", myItems);
        } catch (Exception e) {
            return new AuctionDatabaseResponse(2, "Internal Server Error", new ArrayList<>());
        }
    }

    @Override
    public AuctionDatabaseResponse pushAuctionUpdate(AuctionUpdate auctionUpdate) {
        try {
            // 1. Find the item being updated
            Item existingItem = auctionDAO.fetchItemById(auctionUpdate.getItemId());

            if (existingItem == null) {
                // The item disappeared (this shouldn't happen, but we handle it safely)
                return new AuctionDatabaseResponse(2, "Item not found for update", new ArrayList<>());
            }

            // 2. Apply the live auction updates to our item
            existingItem.setCurrentHighestBid(auctionUpdate.getNewBidAmount());
            existingItem.setHighestBidderUid(auctionUpdate.getNewHighestBidderUid());
            existingItem.setClosed(auctionUpdate.isAuctionEnding());

            // 3. Persist the new state to the database
            boolean isUpdated = auctionDAO.updateItem(existingItem);

            if (isUpdated) {
                // SDD specifies returning error code 0 on success
                return new AuctionDatabaseResponse(0, "Auction state updated successfully", new ArrayList<>());
            } else {
                return new AuctionDatabaseResponse(2, "Failed to update auction state in DB", new ArrayList<>());
            }

        } catch (Exception e) {
            // Error code 2: Internal System Error
            return new AuctionDatabaseResponse(2, "Internal System Error: " + e.getMessage(), new ArrayList<>());
        }
    }
}
package org.yorku.auctionmanager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yorku.auctionmanager.service.AuctionResolver;
import org.yorku.auctionmanager.service.AuctionDatabaseManager;
import org.yorku.auctionmanager.service.AuctionUpdatesPublisher;
import org.yorku.auctionmanager.dto.*;
import org.yorku.auctionmanager.model.*;
import org.yorku.auctionmanager.service.*;

@Service
public class AuctionResolverImpl implements AuctionResolver {

    @Autowired
    private AuctionDatabaseManager dbManager;

    @Autowired
    private AuctionUpdatesPublisher publisher;

    @Override
    public AuctionResolverResponse placeBid(AuthenticatedRequest request) {
        try {
            // 1. CAST the generic request to a specific BidRequest
            BidRequest bidPayload = (BidRequest) request.getRequest();
            
            // Now you can safely get the specific data!
            int targetItemId = bidPayload.getTargetItemId(); 
            double bidAmount = bidPayload.getBidAmount();
            int accountUID = request.getAccountUID();

            // 2. Fetch the current item state from the database
            // Note: You might need a specific fetchItemById method in your DB Manager, 
            // but for now, we'll assume we can retrieve the item.
            Item currentItem = fetchItemHelper(targetItemId); 

            if (currentItem == null) {
                // Item doesn't exist (using Internal Error 2, or you could define a new one)
                return new AuctionResolverResponse(2, "Item not found");
            }

            // 3. Check Error Code 15: Auction Already Closed
            if (currentItem.isClosed()) {
                return new AuctionResolverResponse(15, "Auction Already Closed");
            }

            // 4. Check Error Code 14: Bid Too Low
            if (bidAmount <= currentItem.getCurrentHighestBid()) {
                return new AuctionResolverResponse(14, "Bid Too Low");
            }

            // 5. If validations pass, update the database with the new bid
            currentItem.setCurrentHighestBid(bidAmount);
            currentItem.setHighestBidderUid(request.getAccountUID());
            // dbManager.updateItem(currentItem); // Persist to DB

            // 6. Publish the update to the rest of the system
            publisher.receiveBidUpdate();

            // 7. Return Success (Error code 0)
            return new AuctionResolverResponse(0, "Bid placed successfully");

        } catch (Exception e) {
            // Error code 2: General Internal Server Error
            return new AuctionResolverResponse(2, "Internal Server Error: " + e.getMessage());
        }
    }

    // A helper method to abstract DB fetching for this example
    private Item fetchItemHelper(int itemId) {
        // In reality, this would call your AuctionDatabaseManager/DAO
        return new Item(); 
    }
}
package org.yorku.auctionmanager.service.impl;

import org.springframework.stereotype.Service;
import org.yorku.auctionmanager.service.AuctionResolver;
import org.yorku.auctionmanager.service.AuctionUpdatesPublisher;
import org.yorku.auctionmanager.repository.SqliteAuctionDAO;
import org.yorku.auctionmanager.dto.*;
import org.yorku.auctionmanager.model.*;

@Service
public class AuctionResolverImpl implements AuctionResolver {

    private final SqliteAuctionDAO auctionDAO;
    private final AuctionUpdatesPublisher publisher;

    // Constructor Injection replaces @Autowired
    public AuctionResolverImpl(SqliteAuctionDAO auctionDAO, AuctionUpdatesPublisher publisher) {
        this.auctionDAO = auctionDAO;
        this.publisher = publisher;
    }

    @Override
    public AuctionResolverResponse placeBid(AuthenticatedRequest request) {
        try {
            // 1. CAST the generic request to a specific BidRequest
            BidRequest bidPayload = (BidRequest) request.getRequest();
            
            int targetItemId = bidPayload.getTargetItemId(); 
            double bidAmount = bidPayload.getBidAmount();
            int accountUID = request.getAccountUID();

            // 2. Fetch the REAL item state from the database using our new DAO
            Item currentItem = auctionDAO.fetchItemById(targetItemId); 

            // Error 11: Item Not Found
            if (currentItem == null) {
                return new AuctionResolverResponse(11, "Item not found in database");
            }

            // 3. Check Error Code 15: Auction Already Closed
            if (currentItem.isClosed()) {
                return new AuctionResolverResponse(15, "Auction Already Closed");
            }

            // 4. Check Error Code 14: Bid Too Low
            // The bid must beat either the current highest bid, or the starting price if no bids exist yet
            double priceToBeat = Math.max(currentItem.getCurrentHighestBid(), currentItem.getStartingPrice());
            if (bidAmount <= priceToBeat) {
                return new AuctionResolverResponse(14, "Bid Too Low. Must be higher than: $" + priceToBeat);
            }

            // 5. Update the Java object, then persist to the real database
            currentItem.setCurrentHighestBid(bidAmount);
            currentItem.setHighestBidderUid(accountUID);
            
            boolean updated = auctionDAO.updateItem(currentItem);
            if (!updated) {
                return new AuctionResolverResponse(2, "Database update failed");
            }

            // 6. Publish the update to the rest of the system
            publisher.receiveBidUpdate(); // Note: You might want to pass 'currentItem' into this method later!

            // 7. Return Success (Error code 0)
            return new AuctionResolverResponse(0, "Bid placed successfully");

        } catch (ClassCastException e) {
            // Error 10: Invalid Payload (e.g., they sent a ShipRequest to the Bid endpoint)
            return new AuctionResolverResponse(10, "Invalid Payload Format");
        } catch (Exception e) {
            // Error 2: General Internal Server Error
            return new AuctionResolverResponse(2, "Internal Server Error: " + e.getMessage());
        }
    }
}
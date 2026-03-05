package org.yorku.auctionmanager.model;

public class AuctionUpdate {
    private int itemId;
    private double newBidAmount;
    private int newHighestBidderUid;
    private boolean isAuctionEnding;

    public AuctionUpdate(int itemId, double newBidAmount, int newHighestBidderUid, boolean isAuctionEnding) {
        this.itemId = itemId;
        this.newBidAmount = newBidAmount;
        this.newHighestBidderUid = newHighestBidderUid;
        this.isAuctionEnding = isAuctionEnding;
    }

    // Getters and Setters...
}
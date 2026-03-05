package org.yorku.auctionmanager.model;

public class AuctionUpdate {
    private int itemId;
    public int getItemId() {
		return itemId;
	}

	private double newBidAmount;
    private int newHighestBidderUid;
    private boolean isAuctionEnding;

    public AuctionUpdate(int itemId, double newBidAmount, int newHighestBidderUid, boolean isAuctionEnding) {
        this.itemId = itemId;
        this.newBidAmount = newBidAmount;
        this.newHighestBidderUid = newHighestBidderUid;
        this.isAuctionEnding = isAuctionEnding;
    }
    
    public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public double getNewBidAmount() {
		return newBidAmount;
	}

	public void setNewBidAmount(double newBidAmount) {
		this.newBidAmount = newBidAmount;
	}

	public int getNewHighestBidderUid() {
		return newHighestBidderUid;
	}

	public void setNewHighestBidderUid(int newHighestBidderUid) {
		this.newHighestBidderUid = newHighestBidderUid;
	}

	public boolean isAuctionEnding() {
		return isAuctionEnding;
	}

	public void setAuctionEnding(boolean isAuctionEnding) {
		this.isAuctionEnding = isAuctionEnding;
	}

    
}
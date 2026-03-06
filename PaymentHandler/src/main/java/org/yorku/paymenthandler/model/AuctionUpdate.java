package org.yorku.paymenthandler.model;

public class AuctionUpdate {
	private int winningBidderAccountUID;
	private int itemId;

	public AuctionUpdate() {

	}

	public AuctionUpdate(int winningBidderAccountUID, int itemId) {
		this.winningBidderAccountUID = winningBidderAccountUID;
		this.itemId = itemId;
	}

	public int getWinningBidderAccountUID() {
		return winningBidderAccountUID;
	}

	public void setWinningBidderAccountUID(int winningBidderAccountUID) {
		this.winningBidderAccountUID = winningBidderAccountUID;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
}
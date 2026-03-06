package org.yorku.gatewaymanager.model;

import java.util.ArrayList;
import java.util.List;

public class Auction {
    private int itemId;
    private int ownerUID;
    private int highestBidderUID;
    private String itemName;
    private String description;
    private String auctionType;
    private double startingPrice;
    private double currentBid;
    private long startTime;
    private long endTime;
    private String shippingStatus;
    private boolean closed;
    private List<Bid> bidHistory = new ArrayList<>();

    public Auction() {
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOwnerUID() {
        return ownerUID;
    }

    public void setOwnerUID(int ownerUID) {
        this.ownerUID = ownerUID;
    }

    public int getHighestBidderUID() {
        return highestBidderUID;
    }

    public void setHighestBidderUID(int highestBidderUID) {
        this.highestBidderUID = highestBidderUID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuctionType() {
        return auctionType;
    }

    public void setAuctionType(String auctionType) {
        this.auctionType = auctionType;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public List<Bid> getBidHistory() {
        return bidHistory;
    }

    public void setBidHistory(List<Bid> bidHistory) {
        this.bidHistory = bidHistory;
    }
}

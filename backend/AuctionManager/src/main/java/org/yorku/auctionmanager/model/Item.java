package org.yorku.auctionmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private int id;
    private int ownerUid;
    private String name;
    private String description;
    private double startingPrice;
    private double currentHighestBid;
    private int highestBidderUid;
    private long auctionEndTime; // Timestamp in milliseconds
    
    @JsonProperty("closed")
    private boolean isClosed;

    // Constructors
    public Item() {}

    public Item(int ownerUid, String name, String description, double startingPrice) {
        this.ownerUid = ownerUid;
        this.name = name;
        this.description = description;
        this.startingPrice = startingPrice;
        this.currentHighestBid = startingPrice;
        this.isClosed = false;
        // Default 5 minutes from now if not specified
        this.auctionEndTime = System.currentTimeMillis() + (5 * 60 * 1000);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOwnerUid() { return ownerUid; }
    public void setOwnerUid(int ownerUid) { this.ownerUid = ownerUid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getStartingPrice() { return startingPrice; }
    public void setStartingPrice(double startingPrice) { this.startingPrice = startingPrice; }

    public double getCurrentHighestBid() { return currentHighestBid; }
    public void setCurrentHighestBid(double currentHighestBid) { this.currentHighestBid = currentHighestBid; }

    public int getHighestBidderUid() { return highestBidderUid; }
    public void setHighestBidderUid(int highestBidderUid) { this.highestBidderUid = highestBidderUid; }

    public long getAuctionEndTime() { return auctionEndTime; }
    public void setAuctionEndTime(long auctionEndTime) { this.auctionEndTime = auctionEndTime; }

    @JsonProperty("closed")
    public boolean isClosed() { return isClosed; }

    @JsonProperty("closed")
    public void setClosed(boolean isClosed) { this.isClosed = isClosed; }
}
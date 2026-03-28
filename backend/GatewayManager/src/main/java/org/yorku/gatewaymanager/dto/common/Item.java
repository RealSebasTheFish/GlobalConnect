package org.yorku.gatewaymanager.dto.common;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private int id;
    private int ownerUid;
    private String name;
    private String description;
    private double startingPrice;
    private double currentHighestBid;
    private int highestBidderUid;
    @JsonProperty("closed")
    private boolean closed;

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(int ownerUid) {
        this.ownerUid = ownerUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public double getCurrentHighestBid() {
        return currentHighestBid;
    }

    public void setCurrentHighestBid(double currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
    }

    public int getHighestBidderUid() {
        return highestBidderUid;
    }

    public void setHighestBidderUid(int highestBidderUid) {
        this.highestBidderUid = highestBidderUid;
    }

    @JsonProperty("closed")
    public boolean isClosed() {
        return closed;
    }

    @JsonProperty("closed")
    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}

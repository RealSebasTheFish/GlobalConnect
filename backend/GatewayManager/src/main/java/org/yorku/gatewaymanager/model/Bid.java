package org.yorku.gatewaymanager.model;

public class Bid {
    private int bidId;
    private int itemId;
    private int bidderUID;
    private double amount;
    private long bidTime;

    public Bid() {
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getBidderUID() {
        return bidderUID;
    }

    public void setBidderUID(int bidderUID) {
        this.bidderUID = bidderUID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getBidTime() {
        return bidTime;
    }

    public void setBidTime(long bidTime) {
        this.bidTime = bidTime;
    }
}

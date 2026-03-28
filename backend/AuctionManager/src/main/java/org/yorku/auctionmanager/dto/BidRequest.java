package org.yorku.auctionmanager.dto;

public class BidRequest extends Request {
    private int targetItemId;
    private double bidAmount;

    // Getters and Setters
    public int getTargetItemId() { return targetItemId; }
    public void setTargetItemId(int targetItemId) { this.targetItemId = targetItemId; }
    public double getBidAmount() { return bidAmount; }
    public void setBidAmount(double bidAmount) { this.bidAmount = bidAmount; }
}
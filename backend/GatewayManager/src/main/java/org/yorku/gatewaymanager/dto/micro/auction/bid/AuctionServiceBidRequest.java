package org.yorku.gatewaymanager.dto.micro.auction.bid;

import org.yorku.gatewaymanager.dto.micro.auction.AuctionServiceRequest;

public class AuctionServiceBidRequest extends AuctionServiceRequest {
    private int targetItemId;
    private double bidAmount;

    public AuctionServiceBidRequest() {
    }

    public int getTargetItemId() { return targetItemId; }
    public void setTargetItemId(int targetItemId) { this.targetItemId = targetItemId; }
    public double getBidAmount() { return bidAmount; }
    public void setBidAmount(double bidAmount) { this.bidAmount = bidAmount; }
}

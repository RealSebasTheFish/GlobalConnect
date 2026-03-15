package org.yorku.gatewaymanager.dto.call.bid;


import org.yorku.gatewaymanager.dto.common.AuthenticatedCallRequest;


public class BidCallRequest extends AuthenticatedCallRequest {
    private int targetItemId;
    private double bidAmount;

    public BidCallRequest() {
        super("BidCallRequest");
    }

    public int getTargetItemId() {
        return targetItemId;
    }

    public void setTargetItemId(int targetItemId) {
        this.targetItemId = targetItemId;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

}
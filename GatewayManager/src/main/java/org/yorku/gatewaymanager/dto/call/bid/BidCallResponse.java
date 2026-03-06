package org.yorku.gatewaymanager.dto.call.bid;


import org.yorku.gatewaymanager.dto.common.Response;


public class BidCallResponse extends Response {
    private int targetItemId;
    private double bidAmount;

    public BidCallResponse() {
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
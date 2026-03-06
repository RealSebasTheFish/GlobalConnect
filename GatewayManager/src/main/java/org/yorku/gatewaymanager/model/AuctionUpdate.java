package org.yorku.gatewaymanager.model;

public class AuctionUpdate {
    private int itemId;
    private int sellerUID;
    private int highestBidderUID;
    private double latestBid;
    private boolean closed;
    private boolean paymentPending;
    private boolean paid;
    private boolean shipped;
    private long eventTime;
    private String updateType;
    private String message;

    public AuctionUpdate() {
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSellerUID() {
        return sellerUID;
    }

    public void setSellerUID(int sellerUID) {
        this.sellerUID = sellerUID;
    }

    public int getHighestBidderUID() {
        return highestBidderUID;
    }

    public void setHighestBidderUID(int highestBidderUID) {
        this.highestBidderUID = highestBidderUID;
    }

    public double getLatestBid() {
        return latestBid;
    }

    public void setLatestBid(double latestBid) {
        this.latestBid = latestBid;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isPaymentPending() {
        return paymentPending;
    }

    public void setPaymentPending(boolean paymentPending) {
        this.paymentPending = paymentPending;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isShipped() {
        return shipped;
    }

    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

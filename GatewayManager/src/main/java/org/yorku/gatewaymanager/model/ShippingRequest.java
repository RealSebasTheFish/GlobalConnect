package org.yorku.gatewaymanager.model;

public class ShippingRequest {
    private int itemId;
    private int accountUID;
    private Address destination;
    private boolean expeditedShipping;
    private String courier;

    public ShippingRequest() {
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getAccountUID() {
        return accountUID;
    }

    public void setAccountUID(int accountUID) {
        this.accountUID = accountUID;
    }

    public Address getDestination() {
        return destination;
    }

    public void setDestination(Address destination) {
        this.destination = destination;
    }

    public boolean isExpeditedShipping() {
        return expeditedShipping;
    }

    public void setExpeditedShipping(boolean expeditedShipping) {
        this.expeditedShipping = expeditedShipping;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }
}

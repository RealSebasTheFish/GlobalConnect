package org.yorku.auctionmanager.dto;

import org.yorku.auctionmanager.model.Item;

public class ShipRequest extends Request {
    private Item targetItem;
    private String shippingAddress;

    // Getters and Setters
    public Item getTargetItem() { return targetItem; }
    public void setTargetItem(Item targetItem) { this.targetItem = targetItem; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}
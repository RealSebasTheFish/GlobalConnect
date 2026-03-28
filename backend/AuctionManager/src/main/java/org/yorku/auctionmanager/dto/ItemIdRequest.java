package org.yorku.auctionmanager.dto;

public class ItemIdRequest extends Request {
    private int itemId;

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
}
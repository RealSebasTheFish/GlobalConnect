package org.yorku.auctionmanager.dto;

import org.yorku.auctionmanager.model.Item;

public class ItemRequest extends Request {
    private Item item;

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
}
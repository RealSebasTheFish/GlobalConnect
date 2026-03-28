package org.yorku.gatewaymanager.dto.micro.auction.additem;

import org.yorku.gatewaymanager.dto.common.Item;
import org.yorku.gatewaymanager.dto.micro.auction.AuctionServiceRequest;

public class AuctionServiceAddItemRequest extends AuctionServiceRequest {
    private Item item;

    public AuctionServiceAddItemRequest() {
    }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
}

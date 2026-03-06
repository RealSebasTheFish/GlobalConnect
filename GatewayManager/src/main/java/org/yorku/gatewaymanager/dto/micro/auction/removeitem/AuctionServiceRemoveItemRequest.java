package org.yorku.gatewaymanager.dto.micro.auction.removeitem;

import org.yorku.gatewaymanager.dto.micro.auction.AuctionServiceRequest;

public class AuctionServiceRemoveItemRequest extends AuctionServiceRequest {
    private int itemId;

    public AuctionServiceRemoveItemRequest() {
    }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
}

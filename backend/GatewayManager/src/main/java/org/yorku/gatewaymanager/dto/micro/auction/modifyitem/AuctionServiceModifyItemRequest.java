package org.yorku.gatewaymanager.dto.micro.auction.modifyitem;

import org.yorku.gatewaymanager.dto.common.Item;
import org.yorku.gatewaymanager.dto.micro.auction.AuctionServiceRequest;

public class AuctionServiceModifyItemRequest extends AuctionServiceRequest {
    private Item item;

    public AuctionServiceModifyItemRequest() {
    }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
}

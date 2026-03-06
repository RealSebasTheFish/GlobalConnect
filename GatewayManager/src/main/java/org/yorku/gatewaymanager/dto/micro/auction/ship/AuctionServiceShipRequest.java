package org.yorku.gatewaymanager.dto.micro.auction.ship;

import org.yorku.gatewaymanager.dto.common.Item;
import org.yorku.gatewaymanager.dto.micro.auction.AuctionServiceRequest;

public class AuctionServiceShipRequest extends AuctionServiceRequest {
    private Item targetItem;
    private String shippingAddress;

    public AuctionServiceShipRequest() {
    }

    public Item getTargetItem() { return targetItem; }
    public void setTargetItem(Item targetItem) { this.targetItem = targetItem; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}

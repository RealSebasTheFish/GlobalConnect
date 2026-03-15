package org.yorku.gatewaymanager.dto.call.ship;


import org.yorku.gatewaymanager.dto.common.AuthenticatedCallRequest;
import org.yorku.gatewaymanager.dto.common.Item;


public class ShipCallRequest extends AuthenticatedCallRequest {
    private Item targetItem;
    private String shippingAddress;

    public ShipCallRequest() {
        super("ShipCallRequest");
    }

    public Item getTargetItem() {
        return targetItem;
    }

    public void setTargetItem(Item targetItem) {
        this.targetItem = targetItem;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

}
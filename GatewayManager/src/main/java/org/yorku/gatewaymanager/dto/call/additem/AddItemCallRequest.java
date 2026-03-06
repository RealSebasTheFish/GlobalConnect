package org.yorku.gatewaymanager.dto.call.additem;


import org.yorku.gatewaymanager.dto.common.AuthenticatedCallRequest;
import org.yorku.gatewaymanager.dto.common.Item;


public class AddItemCallRequest extends AuthenticatedCallRequest {
    private Item item;

    public AddItemCallRequest() {
        super("AddItemCallRequest");
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
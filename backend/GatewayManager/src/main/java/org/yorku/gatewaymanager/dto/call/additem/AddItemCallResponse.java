package org.yorku.gatewaymanager.dto.call.additem;


import org.yorku.gatewaymanager.dto.common.Item;
import org.yorku.gatewaymanager.dto.common.Response;


public class AddItemCallResponse extends Response {
    private Item item;

    public AddItemCallResponse() {
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
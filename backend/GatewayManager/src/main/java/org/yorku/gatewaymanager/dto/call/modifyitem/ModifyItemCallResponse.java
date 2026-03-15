package org.yorku.gatewaymanager.dto.call.modifyitem;


import org.yorku.gatewaymanager.dto.common.Item;
import org.yorku.gatewaymanager.dto.common.Response;


public class ModifyItemCallResponse extends Response {
    private Item item;

    public ModifyItemCallResponse() {
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
package org.yorku.gatewaymanager.dto.call.modifyitem;


import org.yorku.gatewaymanager.dto.common.AuthenticatedCallRequest;
import org.yorku.gatewaymanager.dto.common.Item;


public class ModifyItemCallRequest extends AuthenticatedCallRequest {
    private Item item;

    public ModifyItemCallRequest() {
        super("ModifyItemCallRequest");
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
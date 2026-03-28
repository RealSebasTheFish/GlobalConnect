package org.yorku.gatewaymanager.dto.call.removeitem;


import org.yorku.gatewaymanager.dto.common.AuthenticatedCallRequest;


public class RemoveItemCallRequest extends AuthenticatedCallRequest {
    private int itemId;

    public RemoveItemCallRequest() {
        super("RemoveItemCallRequest");
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

}
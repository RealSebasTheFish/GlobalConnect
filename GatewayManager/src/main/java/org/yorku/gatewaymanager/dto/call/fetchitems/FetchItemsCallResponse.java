package org.yorku.gatewaymanager.dto.call.fetchitems;


import java.util.List;
import org.yorku.gatewaymanager.dto.common.Item;
import org.yorku.gatewaymanager.dto.common.Response;


public class FetchItemsCallResponse extends Response {
    private List items;

    public FetchItemsCallResponse() {
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

}
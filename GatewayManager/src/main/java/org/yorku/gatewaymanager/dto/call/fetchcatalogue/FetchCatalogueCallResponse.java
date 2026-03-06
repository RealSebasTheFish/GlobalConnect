package org.yorku.gatewaymanager.dto.call.fetchcatalogue;


import java.util.List;
import org.yorku.gatewaymanager.dto.common.Item;
import org.yorku.gatewaymanager.dto.common.Response;


public class FetchCatalogueCallResponse extends Response {
    private List items;

    public FetchCatalogueCallResponse() {
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

}
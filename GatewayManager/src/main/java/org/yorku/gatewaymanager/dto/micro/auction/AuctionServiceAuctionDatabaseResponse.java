package org.yorku.gatewaymanager.dto.micro.auction;

import java.util.ArrayList;
import java.util.List;
import org.yorku.gatewaymanager.dto.common.Item;

public class AuctionServiceAuctionDatabaseResponse extends AuctionServiceBaseResponse {
    private List<Item> items = new ArrayList<>();

    public AuctionServiceAuctionDatabaseResponse() {
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

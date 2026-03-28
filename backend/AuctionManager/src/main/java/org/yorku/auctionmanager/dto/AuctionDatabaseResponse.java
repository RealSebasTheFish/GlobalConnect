package org.yorku.auctionmanager.dto;

import org.yorku.auctionmanager.model.Item;
import java.util.List;

public class AuctionDatabaseResponse extends BaseResponse {
    
    private List<Item> items;

    // Constructor 1: The Strict SDD Version (Error Code + List)
    // We pass an empty string "" to the BaseResponse for the message
    public AuctionDatabaseResponse(int errorCode, List<Item> items) {
        super(errorCode, ""); 
        this.items = items;
    }

    // Constructor 2: The Detailed Version (Error Code + Message + List)
    public AuctionDatabaseResponse(int errorCode, String message, List<Item> items) {
        super(errorCode, message);
        this.items = items;
    }

    // Getters and Setters
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
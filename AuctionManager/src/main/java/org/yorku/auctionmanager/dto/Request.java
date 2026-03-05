package org.yorku.auctionmanager.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// This tells Spring Boot: "Look for a JSON field called 'type' to figure out which subclass to use"
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BidRequest.class, name = "bid"),
    @JsonSubTypes.Type(value = ShipRequest.class, name = "ship"),
    @JsonSubTypes.Type(value = ItemRequest.class, name = "item"),
    @JsonSubTypes.Type(value = ItemIdRequest.class, name = "itemId")
})
public abstract class Request {
}
package org.yorku.gatewaymanager.dto.micro.auction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.yorku.gatewaymanager.dto.micro.auction.additem.AuctionServiceAddItemRequest;
import org.yorku.gatewaymanager.dto.micro.auction.bid.AuctionServiceBidRequest;
import org.yorku.gatewaymanager.dto.micro.auction.fetchcatalogue.AuctionServiceFetchCatalogueRequest;
import org.yorku.gatewaymanager.dto.micro.auction.modifyitem.AuctionServiceModifyItemRequest;
import org.yorku.gatewaymanager.dto.micro.auction.removeitem.AuctionServiceRemoveItemRequest;
import org.yorku.gatewaymanager.dto.micro.auction.ship.AuctionServiceShipRequest;
import org.yorku.gatewaymanager.dto.micro.auction.fetchitems.AuctionServiceFetchItemsRequest;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AuctionServiceBidRequest.class, name = "bid"),
    @JsonSubTypes.Type(value = AuctionServiceShipRequest.class, name = "ship"),
    @JsonSubTypes.Type(value = AuctionServiceAddItemRequest.class, name = "item"),
    @JsonSubTypes.Type(value = AuctionServiceModifyItemRequest.class, name = "item"),
    @JsonSubTypes.Type(value = AuctionServiceRemoveItemRequest.class, name = "itemId"),
    @JsonSubTypes.Type(value = AuctionServiceFetchCatalogueRequest.class, name = "catalogue"),
    @JsonSubTypes.Type(value = AuctionServiceFetchItemsRequest.class, name = "fetchItems")
})
public abstract class AuctionServiceRequest {
}

package org.yorku.gatewaymanager.dto.micro.auction.fetchcatalogue;

import org.yorku.gatewaymanager.dto.micro.auction.AuctionServiceRequest;

public class AuctionServiceFetchCatalogueRequest extends AuctionServiceRequest {
    private String keyword;

    public AuctionServiceFetchCatalogueRequest() {
    }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
}

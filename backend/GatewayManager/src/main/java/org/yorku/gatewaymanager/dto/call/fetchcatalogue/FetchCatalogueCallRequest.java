package org.yorku.gatewaymanager.dto.call.fetchcatalogue;


import org.yorku.gatewaymanager.dto.common.Request;


public class FetchCatalogueCallRequest extends Request {
    private String keyword;

    public FetchCatalogueCallRequest() {
        super("FetchCatalogueCallRequest");
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
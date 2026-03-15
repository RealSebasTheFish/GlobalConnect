package org.yorku.auctionmanager.dto;

import java.util.List;

import org.yorku.auctionmanager.model.Item;

public class AuctionResolverResponse extends BaseResponse {
	
	// Default constructor to avoid Jackson/SpringBoot serialize crashes
	public AuctionResolverResponse() {
    }

    public AuctionResolverResponse(int errorCode, String message) {
        super(errorCode,message);
    }
    
    
}
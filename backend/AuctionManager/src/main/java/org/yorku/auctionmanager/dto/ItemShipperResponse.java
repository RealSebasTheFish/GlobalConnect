package org.yorku.auctionmanager.dto;

public class ItemShipperResponse extends BaseResponse {
    
	// Default constructor to avoid Jackson/SpringBoot serialize crashes
	public ItemShipperResponse() {
    }
	
    public ItemShipperResponse(int errorCode, String message) {
        super(errorCode, message);
    }
    
}
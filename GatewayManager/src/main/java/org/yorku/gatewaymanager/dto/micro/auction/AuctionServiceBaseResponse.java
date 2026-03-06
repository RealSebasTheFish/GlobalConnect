package org.yorku.gatewaymanager.dto.micro.auction;

public class AuctionServiceBaseResponse {
    private int errorCode;
    private String message;

    public AuctionServiceBaseResponse() {
    }

    public AuctionServiceBaseResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() { return errorCode; }
    public void setErrorCode(int errorCode) { this.errorCode = errorCode; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

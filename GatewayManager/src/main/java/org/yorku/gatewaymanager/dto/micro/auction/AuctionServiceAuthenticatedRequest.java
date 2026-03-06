package org.yorku.gatewaymanager.dto.micro.auction;

public class AuctionServiceAuthenticatedRequest {
    private String secret;
    private AuctionServiceRequest request;
    private int accountUID;

    public AuctionServiceAuthenticatedRequest() {
    }

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    public AuctionServiceRequest getRequest() { return request; }
    public void setRequest(AuctionServiceRequest request) { this.request = request; }
    public int getAccountUID() { return accountUID; }
    public void setAccountUID(int accountUID) { this.accountUID = accountUID; }
}

package org.yorku.auctionmanager.dto;

public class AuthenticatedRequest {
    private String secret;
    private Request request; // The generic request payload
    private int accountUID;  // The ID of the authenticated user

    public AuthenticatedRequest() {}

    // Getters and Setters
    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    
    public Request getRequest() { return request; }
    public void setRequest(Request request) { this.request = request; }

    public int getAccountUID() { return accountUID; }
    public void setAccountUID(int accountUID) { this.accountUID = accountUID; }
}
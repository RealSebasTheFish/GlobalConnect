package org.yorku.gatewaymanager.model;

public class Authentication {
    private String sessionToken;
    private String secret;
    private boolean authenticated;
    private int accountUID;
    private String requestType;
    private long authenticatedAt;

    public Authentication() {
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public int getAccountUID() {
        return accountUID;
    }

    public void setAccountUID(int accountUID) {
        this.accountUID = accountUID;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public long getAuthenticatedAt() {
        return authenticatedAt;
    }

    public void setAuthenticatedAt(long authenticatedAt) {
        this.authenticatedAt = authenticatedAt;
    }
}

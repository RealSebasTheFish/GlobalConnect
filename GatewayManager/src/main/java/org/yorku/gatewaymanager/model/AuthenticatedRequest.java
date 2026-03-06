package org.yorku.gatewaymanager.model;

import org.yorku.gatewaymanager.dto.common.Request;

public class AuthenticatedRequest {
    private String secret;
    private int accountUID;
    private Request request;

    public AuthenticatedRequest() {
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getAccountUID() {
        return accountUID;
    }

    public void setAccountUID(int accountUID) {
        this.accountUID = accountUID;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}

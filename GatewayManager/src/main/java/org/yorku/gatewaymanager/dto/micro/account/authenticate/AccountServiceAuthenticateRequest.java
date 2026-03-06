package org.yorku.gatewaymanager.dto.micro.account.authenticate;

import org.yorku.gatewaymanager.dto.common.Request;

public class AccountServiceAuthenticateRequest {
    private String sessionToken;
    private Request request;

    public AccountServiceAuthenticateRequest() {
    }

    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    public Request getRequest() { return request; }
    public void setRequest(Request request) { this.request = request; }
}

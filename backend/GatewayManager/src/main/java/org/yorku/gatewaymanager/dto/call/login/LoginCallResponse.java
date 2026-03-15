package org.yorku.gatewaymanager.dto.call.login;


import org.yorku.gatewaymanager.dto.common.Response;


public class LoginCallResponse extends Response {
    private String sessionToken;

    public LoginCallResponse() {
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

}
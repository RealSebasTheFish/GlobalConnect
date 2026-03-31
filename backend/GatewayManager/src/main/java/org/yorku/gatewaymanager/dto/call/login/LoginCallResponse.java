package org.yorku.gatewaymanager.dto.call.login;


import org.yorku.gatewaymanager.dto.common.Response;


public class LoginCallResponse extends Response {
    private String sessionToken;
    private Integer accountUID;

    public LoginCallResponse() {
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Integer getAccountUID() {
        return accountUID;
    }

    public void setAccountUID(Integer accountUID) {
        this.accountUID = accountUID;
    }

}
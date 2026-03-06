package org.yorku.gatewaymanager.dto.call.authenticate;


import org.yorku.gatewaymanager.dto.common.Request;
import org.yorku.gatewaymanager.dto.common.Response;


public class AuthenticateCallResponse extends Response {
    private String secret;
    private Request request;
    private int accountUID;

    public AuthenticateCallResponse() {
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public int getAccountUID() {
        return accountUID;
    }

    public void setAccountUID(int accountUID) {
        this.accountUID = accountUID;
    }

}
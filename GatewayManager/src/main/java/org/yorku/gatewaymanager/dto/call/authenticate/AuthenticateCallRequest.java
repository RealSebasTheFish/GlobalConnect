package org.yorku.gatewaymanager.dto.call.authenticate;


import org.yorku.gatewaymanager.dto.common.Request;


public class AuthenticateCallRequest extends Request {
    private String sessionToken;
    private Request request;

    public AuthenticateCallRequest() {
        super("AuthenticateCallRequest");
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

}
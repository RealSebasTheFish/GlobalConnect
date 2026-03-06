package org.yorku.gatewaymanager.dto.call.authenticate;

public class AuthenticateCallRequest {
    private String sessionToken;
    private AuthenticateEmbeddedRequest request;

    public AuthenticateCallRequest() {
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public AuthenticateEmbeddedRequest getRequest() {
        return request;
    }

    public void setRequest(AuthenticateEmbeddedRequest request) {
        this.request = request;
    }
}
package org.yorku.gatewaymanager.dto.common;

public abstract class AuthenticatedCallRequest extends Request {
    private String sessionToken;

    protected AuthenticatedCallRequest() {
    }

    protected AuthenticatedCallRequest(String requestType) {
        super(requestType);
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}

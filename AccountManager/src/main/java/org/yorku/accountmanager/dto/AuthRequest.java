package org.yorku.accountmanager.dto;

public class AuthRequest {
    private String sessionToken;
    private Request request;

    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }

    public Request getRequest() { return request; }
    public void setRequest(Request request) { this.request = request; }
}
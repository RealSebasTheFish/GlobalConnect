package org.yorku.accountmanager.dto;

public class SessionResponse {
    private int errorCode;
    private String sessionToken;

    public SessionResponse() {}

    public SessionResponse(int errorCode, String sessionToken) {
        this.errorCode = errorCode;
        this.sessionToken = sessionToken;
    }

    public int getErrorCode() { return errorCode; }
    public void setErrorCode(int errorCode) { this.errorCode = errorCode; }

    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
}
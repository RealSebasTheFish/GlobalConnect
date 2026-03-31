package org.yorku.accountmanager.dto;

public class SessionResponse {
    private int errorCode;
    private String sessionToken;
    private Integer accountUID;

    public SessionResponse() {}

    public SessionResponse(int errorCode, String sessionToken) {
        this.errorCode = errorCode;
        this.sessionToken = sessionToken;
    }

    public SessionResponse(int errorCode, String sessionToken, Integer accountUID) {
        this.errorCode = errorCode;
        this.sessionToken = sessionToken;
        this.accountUID = accountUID;
    }

    public int getErrorCode() { return errorCode; }
    public void setErrorCode(int errorCode) { this.errorCode = errorCode; }

    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }

    public Integer getAccountUID() { return accountUID; }
    public void setAccountUID(Integer accountUID) { this.accountUID = accountUID; }
}
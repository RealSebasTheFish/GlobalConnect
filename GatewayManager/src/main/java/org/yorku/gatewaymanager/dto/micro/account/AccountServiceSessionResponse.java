package org.yorku.gatewaymanager.dto.micro.account;

public class AccountServiceSessionResponse {
    private int errorCode;
    private String sessionToken;

    public AccountServiceSessionResponse() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}

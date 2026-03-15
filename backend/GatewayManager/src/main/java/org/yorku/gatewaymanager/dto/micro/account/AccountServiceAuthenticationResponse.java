package org.yorku.gatewaymanager.dto.micro.account;

public class AccountServiceAuthenticationResponse {
    private int errorCode;
    private AccountServiceAuthenticatedRequest authenticatedRequest;

    public AccountServiceAuthenticationResponse() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public AccountServiceAuthenticatedRequest getAuthenticatedRequest() {
        return authenticatedRequest;
    }

    public void setAuthenticatedRequest(AccountServiceAuthenticatedRequest authenticatedRequest) {
        this.authenticatedRequest = authenticatedRequest;
    }
}

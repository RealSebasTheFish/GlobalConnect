package org.yorku.accountmanager.dto;

public class AuthenticationResponse {
    private int errorCode;
    private AuthenticatedRequest authenticatedRequest;

    public AuthenticationResponse() {}

    public AuthenticationResponse(int errorCode, AuthenticatedRequest authenticatedRequest) {
        this.errorCode = errorCode;
        this.authenticatedRequest = authenticatedRequest;
    }

    public int getErrorCode() { return errorCode; }
    public void setErrorCode(int errorCode) { this.errorCode = errorCode; }

    public AuthenticatedRequest getAuthenticatedRequest() { return authenticatedRequest; }
    public void setAuthenticatedRequest(AuthenticatedRequest authenticatedRequest) { this.authenticatedRequest = authenticatedRequest; }
}
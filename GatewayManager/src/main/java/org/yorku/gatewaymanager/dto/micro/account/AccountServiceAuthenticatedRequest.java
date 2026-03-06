package org.yorku.gatewaymanager.dto.micro.account;

public class AccountServiceAuthenticatedRequest extends AccountServiceRequest {
    private String secret;
    private AccountServiceRequest request;

    public AccountServiceAuthenticatedRequest() {
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public AccountServiceRequest getRequest() {
        return request;
    }

    public void setRequest(AccountServiceRequest request) {
        this.request = request;
    }
}

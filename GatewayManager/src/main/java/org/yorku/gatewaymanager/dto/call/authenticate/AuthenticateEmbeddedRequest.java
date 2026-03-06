package org.yorku.gatewaymanager.dto.call.authenticate;

public class AuthenticateEmbeddedRequest {
    private Integer accountUID;
    private String requestType;

    public AuthenticateEmbeddedRequest() {
    }

    public Integer getAccountUID() {
        return accountUID;
    }

    public void setAccountUID(Integer accountUID) {
        this.accountUID = accountUID;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
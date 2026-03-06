package org.yorku.gatewaymanager.dto.micro.account;

public class AccountServiceRequest {
    private int accountUID;

    public AccountServiceRequest() {
    }

    public int getAccountUID() {
        return accountUID;
    }

    public void setAccountUID(int accountUID) {
        this.accountUID = accountUID;
    }
}

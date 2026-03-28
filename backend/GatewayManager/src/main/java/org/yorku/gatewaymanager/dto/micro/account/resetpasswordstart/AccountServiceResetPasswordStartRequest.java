package org.yorku.gatewaymanager.dto.micro.account.resetpasswordstart;

public class AccountServiceResetPasswordStartRequest {
    private int accountUID;

    public AccountServiceResetPasswordStartRequest() {
    }

    public int getAccountUID() { return accountUID; }
    public void setAccountUID(int accountUID) { this.accountUID = accountUID; }
}

package org.yorku.gatewaymanager.dto.micro.account.login;

public class AccountServiceLoginRequest {
    private int accountUID;
    private String password;

    public AccountServiceLoginRequest() {
    }

    public int getAccountUID() { return accountUID; }
    public void setAccountUID(int accountUID) { this.accountUID = accountUID; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

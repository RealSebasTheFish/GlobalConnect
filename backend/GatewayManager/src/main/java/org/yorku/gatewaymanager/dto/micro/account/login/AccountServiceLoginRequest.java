package org.yorku.gatewaymanager.dto.micro.account.login;

public class AccountServiceLoginRequest {
    private String username;
    private String password;

    public AccountServiceLoginRequest() {
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

package org.yorku.accountmanager.dto;

public class LoginRequest {
    private int accountUID;      // your spec uses accountUID in checkCreds/createSession
    private String password;     // raw; you will hash before checking

    public int getAccountUID() { return accountUID; }
    public void setAccountUID(int accountUID) { this.accountUID = accountUID; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
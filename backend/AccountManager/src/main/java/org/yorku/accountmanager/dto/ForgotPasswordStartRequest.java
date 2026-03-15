package org.yorku.accountmanager.dto;

public class ForgotPasswordStartRequest {
    private int accountUID;

    public int getAccountUID() { return accountUID; }
    public void setAccountUID(int accountUID) { this.accountUID = accountUID; }
}
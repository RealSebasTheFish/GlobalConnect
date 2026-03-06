package org.yorku.accountmanager.dto;

public class ForgotPasswordRequest {
    private int accountUID;
    private String forgotPasswordRescueCode;
    private String newPassword; // added

    public int getAccountUID() { return accountUID; }
    public void setAccountUID(int accountUID) { this.accountUID = accountUID; }

    public String getForgotPasswordRescueCode() { return forgotPasswordRescueCode; }
    public void setForgotPasswordRescueCode(String forgotPasswordRescueCode) {
        this.forgotPasswordRescueCode = forgotPasswordRescueCode;
    }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
package org.yorku.accountmanager.dto;

public class ForgotPasswordStartResponse {
    private int errorCode;     // 0 success, 2 server error
    private String rescueCode; // returned for demo

    public ForgotPasswordStartResponse() {}

    public ForgotPasswordStartResponse(int errorCode, String rescueCode) {
        this.errorCode = errorCode;
        this.rescueCode = rescueCode;
    }

    public int getErrorCode() { return errorCode; }
    public void setErrorCode(int errorCode) { this.errorCode = errorCode; }

    public String getRescueCode() { return rescueCode; }
    public void setRescueCode(String rescueCode) { this.rescueCode = rescueCode; }
}
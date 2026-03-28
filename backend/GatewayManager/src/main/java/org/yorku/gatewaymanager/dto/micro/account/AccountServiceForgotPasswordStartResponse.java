package org.yorku.gatewaymanager.dto.micro.account;

public class AccountServiceForgotPasswordStartResponse {
    private int errorCode;
    private String rescueCode;

    public AccountServiceForgotPasswordStartResponse() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getRescueCode() {
        return rescueCode;
    }

    public void setRescueCode(String rescueCode) {
        this.rescueCode = rescueCode;
    }
}

package org.yorku.gatewaymanager.dto.call.resetpassword;

import org.yorku.gatewaymanager.dto.common.Response;

public class ResetPasswordStartCallResponse extends Response {
    private String forgotPasswordRescueCode;

    public ResetPasswordStartCallResponse() {
    }

    public String getForgotPasswordRescueCode() {
        return forgotPasswordRescueCode;
    }

    public void setForgotPasswordRescueCode(String forgotPasswordRescueCode) {
        this.forgotPasswordRescueCode = forgotPasswordRescueCode;
    }
}

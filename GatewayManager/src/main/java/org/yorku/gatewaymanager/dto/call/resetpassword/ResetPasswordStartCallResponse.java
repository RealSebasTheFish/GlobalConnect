package org.yorku.gatewaymanager.dto.call.resetpassword;

import org.yorku.gatewaymanager.dto.common.Response;

public class ResetPasswordStartCallResponse extends Response {
    private String rescueCode;

    public ResetPasswordStartCallResponse() {
    }

    public String getRescueCode() {
        return rescueCode;
    }

    public void setRescueCode(String rescueCode) {
        this.rescueCode = rescueCode;
    }
}

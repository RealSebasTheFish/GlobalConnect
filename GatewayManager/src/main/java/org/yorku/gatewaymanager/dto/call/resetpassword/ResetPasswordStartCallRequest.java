package org.yorku.gatewaymanager.dto.call.resetpassword;

import org.yorku.gatewaymanager.dto.common.Request;

public class ResetPasswordStartCallRequest extends Request {
    private int accountUID;

    public ResetPasswordStartCallRequest() {
        super("ResetPasswordStartCallRequest");
    }

    public int getAccountUID() {
        return accountUID;
    }

    public void setAccountUID(int accountUID) {
        this.accountUID = accountUID;
    }
}

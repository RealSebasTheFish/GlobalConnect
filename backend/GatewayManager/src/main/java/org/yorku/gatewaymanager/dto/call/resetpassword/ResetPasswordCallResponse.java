package org.yorku.gatewaymanager.dto.call.resetpassword;

import org.yorku.gatewaymanager.dto.common.Response;
import org.yorku.gatewaymanager.dto.common.Account;

public class ResetPasswordCallResponse extends Response {
    private Account account;

    public ResetPasswordCallResponse() {
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

package org.yorku.gatewaymanager.dto.call.signup;


import org.yorku.gatewaymanager.dto.common.Account;
import org.yorku.gatewaymanager.dto.common.Response;


public class SignUpCallResponse extends Response {
    private Account account;
    private String sessionToken;

    public SignUpCallResponse() {
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

}
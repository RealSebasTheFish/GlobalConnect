package org.yorku.gatewaymanager.dto.call.fetchaccount;


import org.yorku.gatewaymanager.dto.common.Account;
import org.yorku.gatewaymanager.dto.common.Response;


public class FetchAccountCallResponse extends Response {
    private Account account;

    public FetchAccountCallResponse() {
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
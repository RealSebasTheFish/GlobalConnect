package org.yorku.gatewaymanager.dto.call.login;


import org.yorku.gatewaymanager.dto.common.Request;


public class LoginCallRequest extends Request {
    private int accountUID;
    private String password;

    public LoginCallRequest() {
        super("LoginCallRequest");
    }

    public int getAccountUID() {
        return accountUID;
    }

    public void setAccountUID(int accountUID) {
        this.accountUID = accountUID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
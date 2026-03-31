package org.yorku.gatewaymanager.dto.call.login;


import org.yorku.gatewaymanager.dto.common.Request;


public class LoginCallRequest extends Request {
    private String username;
    private String password;

    public LoginCallRequest() {
        super("LoginCallRequest");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
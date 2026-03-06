package org.yorku.gatewaymanager.dto.common;

public abstract class Request {
    private String auth;
    private String requestType;
    private int accountUID;

    protected Request() {
    }

    protected Request(String requestType) {
        this.requestType = requestType;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    
    public int getAccountUID() { 
    	return accountUID; 
    }
    
    public void setAccountUID(int accountUID) { 
    	this.accountUID = accountUID; 
    }
}

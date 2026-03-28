package org.yorku.gatewaymanager.model;

public class ErrorInfo {
    private int errorCode;
    private String errorKey;
    private String message;

    public ErrorInfo() {
    }

    public ErrorInfo(int errorCode, String errorKey, String message) {
        this.errorCode = errorCode;
        this.errorKey = errorKey;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

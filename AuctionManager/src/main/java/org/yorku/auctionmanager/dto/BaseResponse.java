package org.yorku.auctionmanager.dto;

public class BaseResponse {
    private int errorCode;
    private String message;

    // Default constructor
    public BaseResponse() {
    }

    // Parameterized constructor
    public BaseResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    // Getters and Setters
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
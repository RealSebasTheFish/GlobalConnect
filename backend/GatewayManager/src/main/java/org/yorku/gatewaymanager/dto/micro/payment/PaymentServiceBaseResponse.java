package org.yorku.gatewaymanager.dto.micro.payment;

public class PaymentServiceBaseResponse {
    private int errorCode;

    public PaymentServiceBaseResponse() {
    }

    public PaymentServiceBaseResponse(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

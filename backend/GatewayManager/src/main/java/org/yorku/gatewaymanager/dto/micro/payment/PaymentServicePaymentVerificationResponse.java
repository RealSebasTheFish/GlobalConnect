package org.yorku.gatewaymanager.dto.micro.payment;

public class PaymentServicePaymentVerificationResponse extends PaymentServiceBaseResponse {
    private String verificationToken;

    public PaymentServicePaymentVerificationResponse() {
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
}

package org.yorku.gatewaymanager.dto.micro.payment;

public class PaymentServiceAuthenticatedRequest {
    private String secret;
    private PaymentServiceRequest request;

    public PaymentServiceAuthenticatedRequest() {
    }

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    public PaymentServiceRequest getRequest() { return request; }
    public void setRequest(PaymentServiceRequest request) { this.request = request; }
}

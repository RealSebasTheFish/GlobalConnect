package org.yorku.gatewaymanager.dto.micro.payment;

import org.yorku.gatewaymanager.dto.common.Receipt;

public class PaymentServicePaymentResponse extends PaymentServiceBaseResponse {
    private Receipt receipt;

    public PaymentServicePaymentResponse() {
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
}

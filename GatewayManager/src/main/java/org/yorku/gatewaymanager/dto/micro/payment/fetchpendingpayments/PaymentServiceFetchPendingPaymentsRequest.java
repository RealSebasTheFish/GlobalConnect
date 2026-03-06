package org.yorku.gatewaymanager.dto.micro.payment.fetchpendingpayments;

import org.yorku.gatewaymanager.dto.micro.payment.PaymentServiceRequest;

public class PaymentServiceFetchPendingPaymentsRequest extends PaymentServiceRequest {
    private int accountUID;

    public PaymentServiceFetchPendingPaymentsRequest() {
        super("FetchPendingPaymentsRequest");
    }

    public int getAccountUID() { return accountUID; }
    public void setAccountUID(int accountUID) { this.accountUID = accountUID; }
}

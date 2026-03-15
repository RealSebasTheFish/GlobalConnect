package org.yorku.gatewaymanager.dto.micro.payment.fetchreceipts;

import org.yorku.gatewaymanager.dto.micro.payment.PaymentServiceRequest;

public class PaymentServiceFetchReceiptsRequest extends PaymentServiceRequest {
    private int accountUID;

    public PaymentServiceFetchReceiptsRequest() {
        super("FetchReceiptsRequest");
    }

    public int getAccountUID() { return accountUID; }
    public void setAccountUID(int accountUID) { this.accountUID = accountUID; }
}

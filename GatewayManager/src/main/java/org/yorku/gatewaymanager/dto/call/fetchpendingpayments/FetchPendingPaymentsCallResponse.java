package org.yorku.gatewaymanager.dto.call.fetchpendingpayments;


import java.util.List;
import org.yorku.gatewaymanager.dto.common.PendingPayment;
import org.yorku.gatewaymanager.dto.common.Response;


public class FetchPendingPaymentsCallResponse extends Response {
    private List pendingPayments;

    public FetchPendingPaymentsCallResponse() {
    }

    public List getPendingPayments() {
        return pendingPayments;
    }

    public void setPendingPayments(List pendingPayments) {
        this.pendingPayments = pendingPayments;
    }

}
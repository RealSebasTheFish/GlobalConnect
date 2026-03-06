package org.yorku.gatewaymanager.dto.micro.payment;

import java.util.ArrayList;
import java.util.List;
import org.yorku.gatewaymanager.dto.common.PendingPayment;
import org.yorku.gatewaymanager.dto.common.Receipt;

public class PaymentServicePaymentDatabaseResponse extends PaymentServiceBaseResponse {
    private List<Receipt> receipts = new ArrayList<>();
    private List<PendingPayment> pendingPayments = new ArrayList<>();

    public PaymentServicePaymentDatabaseResponse() {
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public List<PendingPayment> getPendingPayments() {
        return pendingPayments;
    }

    public void setPendingPayments(List<PendingPayment> pendingPayments) {
        this.pendingPayments = pendingPayments;
    }
}

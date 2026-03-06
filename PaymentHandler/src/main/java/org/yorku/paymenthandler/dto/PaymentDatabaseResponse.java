package org.yorku.paymenthandler.dto;

import java.util.ArrayList;
import java.util.List;

import org.yorku.paymenthandler.model.PendingPayment;
import org.yorku.paymenthandler.model.Receipt;

public class PaymentDatabaseResponse extends BaseResponse {
	private List<Receipt> receipts = new ArrayList<>();
	private List<PendingPayment> pendingPayments = new ArrayList<>();

	public PaymentDatabaseResponse() {
	}

	public PaymentDatabaseResponse(int errorCode) {
		super(errorCode);
	}

	public List<Receipt> getReceipts() {
		return receipts;
	}

	public void setReceipts(List<Receipt> receipts) {
		this.receipts = (receipts == null) ? new ArrayList<>() : receipts;
	}

	public List<PendingPayment> getPendingPayments() {
		return pendingPayments;
	}

	public void setPendingPayments(List<PendingPayment> pendingPayments) {
		this.pendingPayments = (pendingPayments == null) ? new ArrayList<>() : pendingPayments;
	}
}
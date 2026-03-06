package org.yorku.paymenthandler.dto;

import org.yorku.paymenthandler.model.Receipt;

public class PaymentResponse extends BaseResponse {
	private Receipt receipt;

	public PaymentResponse() {
	}

	public PaymentResponse(int errorCode, Receipt receipt) {
		super(errorCode);
		this.receipt = receipt;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}
}
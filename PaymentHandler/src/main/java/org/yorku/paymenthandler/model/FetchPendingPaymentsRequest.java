package org.yorku.paymenthandler.model;

public class FetchPendingPaymentsRequest extends Request {
	private int accountUID;

	public FetchPendingPaymentsRequest() {
		super("FetchPendingPaymentsRequest");
	}

	public int getAccountUID() {
		return accountUID;
	}

	public void setAccountUID(int accountUID) {
		this.accountUID = accountUID;
	}
}
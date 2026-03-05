package org.yorku.paymenthandler.model;

public class FetchReceiptsRequest extends Request {
	private int accountUID;

	public FetchReceiptsRequest() {
		super("FetchReceiptsRequest");
	}

	public int getAccountUID() {
		return accountUID;
	}

	public void setAccountUID(int accountUID) {
		this.accountUID = accountUID;
	}
}
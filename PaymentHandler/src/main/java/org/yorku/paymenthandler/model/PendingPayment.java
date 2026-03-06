package org.yorku.paymenthandler.model;

public class PendingPayment {
	private int accountUID;
	private int itemId;

	public PendingPayment() {

	}

	public PendingPayment(int accountUID, int itemId) {
		this.accountUID = accountUID;
		this.itemId = itemId;
	}

	public int getAccountUID() {
		return accountUID;
	}

	public void setAccountUID(int accountUID) {
		this.accountUID = accountUID;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
}
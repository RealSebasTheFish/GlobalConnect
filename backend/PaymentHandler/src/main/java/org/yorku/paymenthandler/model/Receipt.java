package org.yorku.paymenthandler.model;

public class Receipt {
	private int accountUID;
	private int itemId;

	// Order Details
	private double amount;
	private String paymentMethod;
	private String date;

	private double shippingCost;
	private boolean expeditedShipping;
	private double expeditedExtraCost;

	public Receipt() {

	}

	public Receipt(int accountUID, int itemId, double amount, String paymentMethod, String date, double shippingCost,
			boolean expeditedShipping, double expeditedExtraCost) {
		this.accountUID = accountUID;
		this.itemId = itemId;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.date = date;
		this.shippingCost = shippingCost;
		this.expeditedShipping = expeditedShipping;
		this.expeditedExtraCost = expeditedExtraCost;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(double shippingCost) {
		this.shippingCost = shippingCost;
	}

	public boolean isExpeditedShipping() {
		return expeditedShipping;
	}

	public void setExpeditedShipping(boolean expeditedShipping) {
		this.expeditedShipping = expeditedShipping;
	}

	public double getExpeditedExtraCost() {
		return expeditedExtraCost;
	}

	public void setExpeditedExtraCost(double expeditedExtraCost) {
		this.expeditedExtraCost = expeditedExtraCost;
	}
}
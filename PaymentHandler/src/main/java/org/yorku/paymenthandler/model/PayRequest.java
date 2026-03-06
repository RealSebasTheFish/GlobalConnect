package org.yorku.paymenthandler.model;

public class PayRequest extends Request {
	private int accountUID;
	private int itemId;

	// Payment Details
	private String cardNumber;
	private String name;
	private String expDate;
	private String securityCode;
	private String paymentMethod;

	// Order Details
	private double amount;
	private double shippingCost;
	private boolean expeditedShipping;
	private double expeditedExtraCost;

	public PayRequest() {
		super("PayRequest");
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

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

package org.yorku.paymenthandler.model;

public class UserPaymentMethod {
	private int accountUID;
	private String paymentMethod;
	private String cardholderName;
	private String last4;
	private String expDate;

	public UserPaymentMethod() {

	}

	public UserPaymentMethod(int accountUID, String paymentMethod, String cardholderName, String last4,
			String expDate) {
		this.accountUID = accountUID;
		this.paymentMethod = paymentMethod;
		this.cardholderName = cardholderName;
		this.last4 = last4;
		this.expDate = expDate;
	}

	public int getAccountUID() {
		return accountUID;
	}

	public void setAccountUID(int accountUID) {
		this.accountUID = accountUID;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getCardholderName() {
		return cardholderName;
	}

	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
}

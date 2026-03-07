package org.yorku.auctionmanager.dto;

public class PaymentRegistrationRequest {
    private int accountUID;
    

	private int itemId;

    public PaymentRegistrationRequest(int accountUID, int itemId) {
        this.accountUID = accountUID;
        this.itemId = itemId;
    }
    // Getters and Setters...
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
package org.yorku.gatewaymanager.dto.common;

public class PendingPayment {
    private int accountUID;
    private int itemId;

    public PendingPayment() {
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

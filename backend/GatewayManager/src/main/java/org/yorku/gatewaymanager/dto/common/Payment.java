package org.yorku.gatewaymanager.dto.common;

public class Payment {
    private int accountUID;
    private int itemId;

    public Payment() {
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

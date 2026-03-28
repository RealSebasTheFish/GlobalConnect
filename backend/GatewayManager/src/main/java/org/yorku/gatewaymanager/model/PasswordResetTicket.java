package org.yorku.gatewaymanager.model;

public class PasswordResetTicket {
    private int accountUID;
    private String rescueCode;
    private boolean valid;
    private long createdAt;
    private long expiresAt;

    public PasswordResetTicket() {
    }

    public int getAccountUID() {
        return accountUID;
    }

    public void setAccountUID(int accountUID) {
        this.accountUID = accountUID;
    }

    public String getRescueCode() {
        return rescueCode;
    }

    public void setRescueCode(String rescueCode) {
        this.rescueCode = rescueCode;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }
}

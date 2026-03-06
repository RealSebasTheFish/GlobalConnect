package org.yorku.gatewaymanager.model;

public class Payment {
    private int accountUID;
    private int itemId;
    private double amount;
    private String paymentMethod;
    private String cardNumber;
    private String cardholderName;
    private String expiryDate;
    private String securityCode;
    private double shippingCost;
    private boolean expeditedShipping;
    private double expeditedExtraCost;

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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
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

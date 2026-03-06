package org.yorku.gatewaymanager.model;

public class Receipt {
    private int accountUID;
    private int itemId;
    private double amount;
    private String paymentMethod;
    private String date;
    private double shippingCost;
    private boolean expeditedShipping;
    private double expeditedExtraCost;
    private String receiptReference;

    public Receipt() {
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

    public String getReceiptReference() {
        return receiptReference;
    }

    public void setReceiptReference(String receiptReference) {
        this.receiptReference = receiptReference;
    }
}

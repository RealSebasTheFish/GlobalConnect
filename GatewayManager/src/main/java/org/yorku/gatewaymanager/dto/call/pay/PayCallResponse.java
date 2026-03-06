package org.yorku.gatewaymanager.dto.call.pay;


import org.yorku.gatewaymanager.dto.common.Receipt;
import org.yorku.gatewaymanager.dto.common.Response;


public class PayCallResponse extends Response {
    private Receipt receipt;

    public PayCallResponse() {
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

}
package org.yorku.gatewaymanager.dto.call.fetchreceipts;


import java.util.List;
import org.yorku.gatewaymanager.dto.common.Receipt;
import org.yorku.gatewaymanager.dto.common.Response;


public class FetchReceiptsCallResponse extends Response {
    private List receipts;

    public FetchReceiptsCallResponse() {
    }

    public List getReceipts() {
        return receipts;
    }

    public void setReceipts(List receipts) {
        this.receipts = receipts;
    }

}
package org.yorku.gatewaymanager.dto.micro.payment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.yorku.gatewaymanager.dto.micro.payment.fetchpendingpayments.PaymentServiceFetchPendingPaymentsRequest;
import org.yorku.gatewaymanager.dto.micro.payment.fetchreceipts.PaymentServiceFetchReceiptsRequest;
import org.yorku.gatewaymanager.dto.micro.payment.pay.PaymentServicePayRequest;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "requestType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PaymentServiceFetchReceiptsRequest.class, name = "FetchReceiptsRequest"),
    @JsonSubTypes.Type(value = PaymentServiceFetchPendingPaymentsRequest.class, name = "FetchPendingPaymentsRequest"),
    @JsonSubTypes.Type(value = PaymentServicePayRequest.class, name = "PayRequest")
})
public abstract class PaymentServiceRequest {
    private String requestType;

    protected PaymentServiceRequest() {
    }

    protected PaymentServiceRequest(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}

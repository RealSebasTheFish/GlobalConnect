package org.yorku.gatewaymanager.dto.call.fetchpendingpayments;


import org.yorku.gatewaymanager.dto.common.AuthenticatedCallRequest;


public class FetchPendingPaymentsCallRequest extends AuthenticatedCallRequest {

    public FetchPendingPaymentsCallRequest() {
        super("FetchPendingPaymentsCallRequest");
    }

}
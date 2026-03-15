package org.yorku.paymenthandler.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "requestType", visible = true)

@JsonSubTypes({ @JsonSubTypes.Type(value = FetchReceiptsRequest.class, name = "FetchReceiptsRequest"),
		@JsonSubTypes.Type(value = FetchPendingPaymentsRequest.class, name = "FetchPendingPaymentsRequest"),
		@JsonSubTypes.Type(value = PayRequest.class, name = "PayRequest") })

public abstract class Request {
	private String requestType;

	protected Request() {

	}

	protected Request(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
}
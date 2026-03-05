package org.yorku.paymenthandler.model;

public class AuthenticatedRequest {
	private String secret;
	private Request request;

	public AuthenticatedRequest() {

	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
}
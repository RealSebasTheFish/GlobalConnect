package org.yorku.paymenthandler.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) // This stops the 400 Bad Request error
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
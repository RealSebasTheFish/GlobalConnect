package org.yorku.paymenthandler.dto;

public class PaymentVerificationResponse extends BaseResponse {
	private String verificationToken;

	public PaymentVerificationResponse() {

	}

	public PaymentVerificationResponse(int errorCode, String verificationToken) {
		super(errorCode);

		this.verificationToken = verificationToken;
	}

	public String getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}
}
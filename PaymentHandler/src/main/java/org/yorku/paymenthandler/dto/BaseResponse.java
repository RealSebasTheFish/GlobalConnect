package org.yorku.paymenthandler.dto;

public class BaseResponse {
	private int errorCode;

	public BaseResponse() {
	}

	public BaseResponse(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
package org.yorku.paymenthandler.service;

import org.yorku.paymenthandler.dto.PaymentResponse;
import org.yorku.paymenthandler.model.AuthenticatedRequest;

public interface PaymentProcessor {
	PaymentResponse pay(AuthenticatedRequest request);
}
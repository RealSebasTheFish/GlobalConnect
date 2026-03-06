package org.yorku.paymenthandler.service;

import org.yorku.paymenthandler.dto.PaymentVerificationResponse;
import org.yorku.paymenthandler.model.Item;

public interface PaymentVerifier {
	PaymentVerificationResponse verifyPayment(Item item, int accountUID);
}
package org.yorku.paymenthandler.service;

import org.yorku.paymenthandler.dto.PaymentDatabaseResponse;
import org.yorku.paymenthandler.model.AuthenticatedRequest;
import org.yorku.paymenthandler.model.Payment;
import org.yorku.paymenthandler.model.Receipt;

public interface PaymentDatabaseManager {
	PaymentDatabaseResponse fetchPaymentData(AuthenticatedRequest request);

	PaymentDatabaseResponse addReceipt(Receipt receipt);

	PaymentDatabaseResponse addPendingPayment(Payment payment);

	PaymentDatabaseResponse removePendingPayment(Payment payment);
}
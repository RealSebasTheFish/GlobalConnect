package org.yorku.paymenthandler.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yorku.paymenthandler.dto.PaymentDatabaseResponse;
import org.yorku.paymenthandler.dto.PaymentResponse;
import org.yorku.paymenthandler.model.AuthenticatedRequest;
import org.yorku.paymenthandler.model.FetchPendingPaymentsRequest;
import org.yorku.paymenthandler.model.FetchReceiptsRequest;
import org.yorku.paymenthandler.service.PaymentDatabaseManager;
import org.yorku.paymenthandler.service.PaymentProcessor;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	private final PaymentDatabaseManager db;
	private final PaymentProcessor processor;

	public PaymentController(PaymentDatabaseManager db, PaymentProcessor processor) {
		this.db = db;
		this.processor = processor;
	}

	@PostMapping("/pay")
	public PaymentResponse pay(@RequestBody AuthenticatedRequest request) {
		return processor.pay(request);
	}

	@PostMapping("/fetchreceipts")
	public PaymentDatabaseResponse fetchReceipts(@RequestBody AuthenticatedRequest request) {
		if (request == null || request.getRequest() == null
				|| !(request.getRequest() instanceof FetchReceiptsRequest)) {
			return new PaymentDatabaseResponse(6);
		}

		return db.fetchPaymentData(request);
	}

	@PostMapping("/fetchpendingpayments")
	public PaymentDatabaseResponse fetchPendingPayments(@RequestBody AuthenticatedRequest request) {
		if (request == null || request.getRequest() == null
				|| !(request.getRequest() instanceof FetchPendingPaymentsRequest)) {
			return new PaymentDatabaseResponse(6);
		}

		return db.fetchPaymentData(request);
	}
}

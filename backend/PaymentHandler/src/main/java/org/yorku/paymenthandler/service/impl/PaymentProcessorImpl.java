package org.yorku.paymenthandler.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.yorku.paymenthandler.dto.PaymentResponse;
import org.yorku.paymenthandler.model.AuthenticatedRequest;
import org.yorku.paymenthandler.model.PayRequest;
import org.yorku.paymenthandler.model.Payment;
import org.yorku.paymenthandler.model.Receipt;
import org.yorku.paymenthandler.model.UserPaymentMethod;
import org.yorku.paymenthandler.repository.PaymentDAO;
import org.yorku.paymenthandler.service.PaymentDatabaseManager;
import org.yorku.paymenthandler.service.PaymentProcessor;

@Service
public class PaymentProcessorImpl implements PaymentProcessor {
	private final PaymentDAO dao;
	private final PaymentDatabaseManager db;

	public PaymentProcessorImpl(PaymentDAO dao, PaymentDatabaseManager db) {
		this.dao = dao;
		this.db = db;
	}

	// Helper Functions

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	private String last4(String cardNumber) {
		if (cardNumber == null)
			return null;

		String digits = cardNumber.replaceAll("\\D", "");

		if (digits.length() < 4)
			return digits;

		return digits.substring(digits.length() - 4);
	}

	@Override
	public PaymentResponse pay(AuthenticatedRequest request) {
		try {
			if (request == null || !(request.getRequest() instanceof PayRequest pr)) {
				return new PaymentResponse(2, null);
			}

			if (isBlank(pr.getCardNumber()) || isBlank(pr.getName()) || isBlank(pr.getExpDate())
					|| isBlank(pr.getSecurityCode()) || isBlank(pr.getPaymentMethod())) {
				return new PaymentResponse(7, null);
			}

			if (pr.getAmount() <= 0 || pr.getShippingCost() < 0 || pr.getExpeditedExtraCost() < 0) {
				return new PaymentResponse(7, null);
			}

			if ((!pr.isExpeditedShipping() && pr.getExpeditedExtraCost() != 0)
					|| !dao.pendingExists(pr.getAccountUID(), pr.getItemId())) {
				return new PaymentResponse(7, null);
			}

			dao.upsertPaymentMethod(new UserPaymentMethod(pr.getAccountUID(), pr.getPaymentMethod(), pr.getName(),
					last4(pr.getCardNumber()), pr.getExpDate()));

			Receipt receipt = new Receipt(pr.getAccountUID(), pr.getItemId(), pr.getAmount(), pr.getPaymentMethod(),
					Instant.now().toString(), pr.getShippingCost(), pr.isExpeditedShipping(),
					pr.getExpeditedExtraCost());

			if (db.addReceipt(receipt).getErrorCode() != 0) {
				return new PaymentResponse(2, null);
			}

			if (db.removePendingPayment(new Payment(pr.getAccountUID(), pr.getItemId())).getErrorCode() != 0) {
				return new PaymentResponse(2, null);
			}

			return new PaymentResponse(0, receipt);

		} catch (Exception e) {
			return new PaymentResponse(2, null);
		}
	}
}
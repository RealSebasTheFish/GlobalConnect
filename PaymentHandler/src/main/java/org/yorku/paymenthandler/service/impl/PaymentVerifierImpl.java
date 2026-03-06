package org.yorku.paymenthandler.service.impl;

import org.springframework.stereotype.Service;
import org.yorku.paymenthandler.dto.PaymentVerificationResponse;
import org.yorku.paymenthandler.model.Item;
import org.yorku.paymenthandler.repository.PaymentDAO;
import org.yorku.paymenthandler.service.PaymentVerifier;

@Service
public class PaymentVerifierImpl implements PaymentVerifier {
	private final PaymentDAO dao;

	public PaymentVerifierImpl(PaymentDAO dao) {
		this.dao = dao;
	}

	@Override
	public PaymentVerificationResponse verifyPayment(Item item, int accountUID) {
		// Error codes:
		// 0: verified token returned
		// 8: payment not found / incomplete
		// 9: verification timeout

		try {
			boolean hasReceipt = dao.receiptExists(accountUID, item.getId());
			boolean stillPending = dao.pendingExists(accountUID, item.getId());

			if (!hasReceipt || stillPending) {
				return new PaymentVerificationResponse(8, null);
			}

			String token = "VERIFIED:" + accountUID + ":" + item.getId();

			return new PaymentVerificationResponse(0, token);
		} catch (Exception e) {
			return new PaymentVerificationResponse(9, null);
		}
	}
}
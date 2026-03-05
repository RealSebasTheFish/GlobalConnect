package org.yorku.paymenthandler.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.yorku.paymenthandler.dto.PaymentDatabaseResponse;
import org.yorku.paymenthandler.model.AuthenticatedRequest;
import org.yorku.paymenthandler.model.FetchPendingPaymentsRequest;
import org.yorku.paymenthandler.model.FetchReceiptsRequest;
import org.yorku.paymenthandler.model.Payment;
import org.yorku.paymenthandler.model.Receipt;
import org.yorku.paymenthandler.repository.PaymentDAO;
import org.yorku.paymenthandler.service.PaymentDatabaseManager;

@Service
public class PaymentDatabaseManagerImpl implements PaymentDatabaseManager {
	private final PaymentDAO dao;

	public PaymentDatabaseManagerImpl(PaymentDAO dao) {
		this.dao = dao;
	}

	@Override
	public PaymentDatabaseResponse fetchPaymentData(AuthenticatedRequest request) {
		if (request == null || request.getRequest() == null) {
			return new PaymentDatabaseResponse(6);
		}

		try {
			PaymentDatabaseResponse resp = new PaymentDatabaseResponse(0);

			if (request.getRequest() instanceof FetchReceiptsRequest r) {
				resp.setReceipts(dao.fetchReceipts(r.getAccountUID()));
				resp.setPendingPayments(List.of());

				return resp;
			}

			if (request.getRequest() instanceof FetchPendingPaymentsRequest r) {
				resp.setPendingPayments(dao.fetchPending(r.getAccountUID()));
				resp.setReceipts(List.of());

				return resp;
			}

			return new PaymentDatabaseResponse(6);
		} catch (Exception e) {
			return new PaymentDatabaseResponse(2);
		}
	}

	@Override
	public PaymentDatabaseResponse addReceipt(Receipt receipt) {
		try {
			dao.addReceipt(receipt);

			return new PaymentDatabaseResponse(0);
		} catch (Exception e) {
			return new PaymentDatabaseResponse(2);
		}
	}

	@Override
	public PaymentDatabaseResponse addPendingPayment(Payment payment) {
		try {
			dao.addPending(payment.getAccountUID(), payment.getItemId());

			return new PaymentDatabaseResponse(0);
		} catch (Exception e) {
			return new PaymentDatabaseResponse(2);
		}
	}

	@Override
	public PaymentDatabaseResponse removePendingPayment(Payment payment) {
		try {
			dao.removePending(payment.getAccountUID(), payment.getItemId());

			return new PaymentDatabaseResponse(0);
		} catch (Exception e) {
			return new PaymentDatabaseResponse(2);
		}
	}
}
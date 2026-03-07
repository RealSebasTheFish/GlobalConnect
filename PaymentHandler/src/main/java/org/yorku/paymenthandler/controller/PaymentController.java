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
import org.yorku.paymenthandler.model.*;

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
	// Added endpoint that was missing to allow Auction microservice to register a pending payment once an auction closes
	@PostMapping("/register-pending")
	public PaymentResponse registerPending(@RequestBody AuthenticatedRequest authRequest) {
	    // 1. Validation: Ensure it is a PayRequest
	    if (authRequest == null || !(authRequest.getRequest() instanceof PayRequest)) {
	        return new PaymentResponse(6, null); 
	    }
	    
	    PayRequest pr = (PayRequest) authRequest.getRequest();
	    
	    try {
	        // 2. Map PayRequest data to the Payment model the manager expects
	        org.yorku.paymenthandler.model.Payment paymentModel = 
	            new org.yorku.paymenthandler.model.Payment(pr.getAccountUID(), pr.getItemId());
	        
	        // 3. Call the correct manager method: addPendingPayment
	        // Assuming 'db' is your PaymentDatabaseManager instance
	        db.addPendingPayment(paymentModel);
	        
	        return new PaymentResponse(0, null);
	    } catch (Exception e) {
	        System.err.println("Error in register-pending: " + e.getMessage());
	        return new PaymentResponse(2, null);
	    }
	}
}

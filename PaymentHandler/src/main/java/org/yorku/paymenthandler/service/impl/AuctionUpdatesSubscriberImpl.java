package org.yorku.paymenthandler.service.impl;

import org.springframework.stereotype.Service;
import org.yorku.paymenthandler.model.AuctionUpdate;
import org.yorku.paymenthandler.model.Payment;
import org.yorku.paymenthandler.service.AuctionUpdatesSubscriber;
import org.yorku.paymenthandler.service.PaymentDatabaseManager;

@Service
public class AuctionUpdatesSubscriberImpl implements AuctionUpdatesSubscriber {
	private final PaymentDatabaseManager db;

	public AuctionUpdatesSubscriberImpl(PaymentDatabaseManager db) {
		this.db = db;
	}

	@Override
	public void recieveAuctionUpdate(AuctionUpdate auctionUpdate) {
		if (auctionUpdate == null)
			return;

		db.addPendingPayment(new Payment(auctionUpdate.getWinningBidderAccountUID(), auctionUpdate.getItemId()));
	}
}
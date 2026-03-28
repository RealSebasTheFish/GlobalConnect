package org.yorku.paymenthandler.service;

import org.yorku.paymenthandler.model.AuctionUpdate;

public interface AuctionUpdatesSubscriber {
	void recieveAuctionUpdate(AuctionUpdate auctionUpdate);
}
package org.yorku.auctionmanager.client;

import org.yorku.auctionmanager.model.Item;

public interface PaymentVerifierClient {

	int verifyPayment(Item targetItem, int accountUID);

}

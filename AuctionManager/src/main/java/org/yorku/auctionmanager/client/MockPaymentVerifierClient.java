package org.yorku.auctionmanager.client;

import org.springframework.stereotype.Service;
import org.yorku.auctionmanager.model.Item;

@Service
public class MockPaymentVerifierClient implements PaymentVerifierClient {

    @Override
    public int verifyPayment(Item targetItem, int accountUID) {
        System.out.println("MOCK PAYMENT CLIENT: Verifying payment for Item " + targetItem.getId());
        // Return 0 to simulate a successful payment so your shipItem curl test works!
        return 0; 
    }
}
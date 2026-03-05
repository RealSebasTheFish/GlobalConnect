package org.yorku.auctionmanager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yorku.auctionmanager.service.ItemShipper;
import org.yorku.auctionmanager.service.AuctionUpdatesPublisher;
import org.yorku.auctionmanager.dto.AuthenticatedRequest;
import org.yorku.auctionmanager.dto.ItemShipperResponse;
import org.yorku.auctionmanager.model.*;
// Assuming we will have a client/proxy interface to talk to the Payment Component
import org.yorku.auctionmanager.client.PaymentVerifierClient; 

@Service
public class ItemShipperImpl implements ItemShipper {

    @Autowired
    private AuctionUpdatesPublisher publisher;

    @Autowired
    private PaymentVerifierClient paymentVerifier; 

    @Override
    public ItemShipperResponse shipItem(AuthenticatedRequest request) {
        try {
            Item targetItem = request.getTargetItem();
            int accountUID = request.getAccountUID();
            String shippingAddress = request.getShippingAddress();

            // 1. Check Error Code 13: Invalid Shipping Address
            if (shippingAddress == null || shippingAddress.trim().isEmpty() || !isValidAddress(shippingAddress)) {
                return new ItemShipperResponse(13, "Invalid Shipping Address");
            }

            // 2. Cross-Component Call: Verify Payment
            // Calls PaymentHandler:PaymentVerifier as specified in your architecture
            int paymentStatus = paymentVerifier.verifyPayment(targetItem, accountUID);

            // Check Error Code 12: Item Not Paid (or Error 8/9 from the PaymentVerifier)
            if (paymentStatus != 0) {
                // You can map the specific payment errors (8, 9) to your Shipper error (12)
                return new ItemShipperResponse(12, "Item Not Paid or Verification Failed");
            }

            // 3. Process the shipping logic (e.g., generating tracking, updating DB state)
            // dbManager.updateItemStatus(targetItem.getId(), "SHIPPED");

            // 4. Publish the shipping update to the system
            publisher.receiveShipUpdate();

            // 5. Return Success (Error code 0)
            return new ItemShipperResponse(0, "Item shipped successfully");

        } catch (Exception e) {
             // Error code 2: General Internal Server Error
             return new ItemShipperResponse(2, "Internal Server Error: " + e.getMessage());
        }
    }

    // Helper method for address validation logic
    private boolean isValidAddress(String address) {
        // Implement regex or external API validation here
        return address.length() > 5;
    }
}
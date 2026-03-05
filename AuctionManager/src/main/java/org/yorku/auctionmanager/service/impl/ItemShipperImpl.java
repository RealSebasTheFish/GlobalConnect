package org.yorku.auctionmanager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yorku.auctionmanager.service.ItemShipper;
import org.yorku.auctionmanager.service.AuctionUpdatesPublisher;
import org.yorku.auctionmanager.repository.AuctionDAO;
import org.yorku.auctionmanager.dto.*;
import org.yorku.auctionmanager.model.*;
import org.yorku.auctionmanager.client.PaymentVerifierClient; 

@Service
public class ItemShipperImpl implements ItemShipper {

    @Autowired
    private AuctionUpdatesPublisher publisher;

    @Autowired
    private PaymentVerifierClient paymentVerifier; 

    @Autowired
    private AuctionDAO auctionDAO; // Added to check DB state safely

    @Override
    public ItemShipperResponse shipItem(AuthenticatedRequest request) {
        try {
            ShipRequest shipPayload = (ShipRequest) request.getRequest();
            Item targetItem = shipPayload.getTargetItem();
            String shippingAddress = shipPayload.getShippingAddress();
            int accountUID = request.getAccountUID();

            // 1. Validate Address (Error 13)
            if (shippingAddress == null || shippingAddress.trim().isEmpty() || !isValidAddress(shippingAddress)) {
                return new ItemShipperResponse(13, "Invalid Shipping Address");
            }

            // 2. Verify Item Exists and Permissions (Errors 11 & 16)
            Item dbItem = auctionDAO.fetchItemById(targetItem.getId());
            if (dbItem == null) {
                return new ItemShipperResponse(11, "Item not found in database");
            }
            if (dbItem.getOwnerUid() != accountUID) {
                return new ItemShipperResponse(16, "Permission Denied: Only the owner can ship this item");
            }

            // 3. Cross-Component Call: Verify Payment
            int paymentStatus = paymentVerifier.verifyPayment(dbItem, accountUID);
            if (paymentStatus != 0) {
                return new ItemShipperResponse(12, "Item Not Paid or Verification Failed");
            }

            // 4. Update the DB state (Optional depending on your SDD, but recommended!)
            // dbItem.setClosed(true); 
            // auctionDAO.updateItem(dbItem);

            // 5. Publish the shipping update
            publisher.receiveShipUpdate();

            return new ItemShipperResponse(0, "Item shipped successfully");

        } catch (ClassCastException e) {
            return new ItemShipperResponse(10, "Invalid Payload Format");
        } catch (Exception e) {
            return new ItemShipperResponse(2, "Internal Server Error: " + e.getMessage());
        }
    }

    private boolean isValidAddress(String address) {
        return address != null && address.length() > 5;
    }
}
package org.yorku.auctionmanager.service.impl;

import org.springframework.stereotype.Service;
import org.yorku.auctionmanager.service.ItemShipper;
import org.yorku.auctionmanager.service.AuctionUpdatesPublisher;
import org.yorku.auctionmanager.repository.SqliteAuctionDAO;
import org.yorku.auctionmanager.dto.*;
import org.yorku.auctionmanager.model.*;
import org.yorku.auctionmanager.client.PaymentVerifierClient; 

@Service
public class ItemShipperImpl implements ItemShipper {

    private final AuctionUpdatesPublisher publisher;
    private final PaymentVerifierClient paymentVerifier; 
    private final SqliteAuctionDAO auctionDAO; 

    public ItemShipperImpl(AuctionUpdatesPublisher publisher, PaymentVerifierClient paymentVerifier, SqliteAuctionDAO auctionDAO) {
        this.publisher = publisher;
        this.paymentVerifier = paymentVerifier;
        this.auctionDAO = auctionDAO;
    }

    @Override
    public ItemShipperResponse shipItem(AuthenticatedRequest request) {
        try {
            ShipRequest shipPayload = (ShipRequest) request.getRequest();
            Item targetItem = shipPayload.getTargetItem();
            String shippingAddress = shipPayload.getShippingAddress();
            int accountUID = request.getAccountUID(); // This is the SELLER

            if (shippingAddress == null || shippingAddress.trim().isEmpty() || !isValidAddress(shippingAddress)) {
                return new ItemShipperResponse(13, "Invalid Shipping Address");
            }

            Item dbItem = auctionDAO.fetchItemById(targetItem.getId());
            if (dbItem == null) {
                return new ItemShipperResponse(11, "Item not found in database");
            }
            if (dbItem.getOwnerUid() != accountUID) {
                return new ItemShipperResponse(16, "Permission Denied: Only the owner can ship this item");
            }

            // Get the BUYER's ID
            int buyerUid = dbItem.getHighestBidderUid();
            if (buyerUid <= 0) {
                return new ItemShipperResponse(14, "Item has no valid buyer");
            }

            // REAL Cross-Component Call: Check if the BUYER has a receipt for this item
            boolean isPaid = paymentVerifier.verifyPayment(dbItem.getId(), buyerUid);
            if (!isPaid) {
                return new ItemShipperResponse(12, "Item Not Paid or Verification Failed");
            }

            // Close the item in the database now that it's shipped
            dbItem.setClosed(true);
            auctionDAO.updateItem(dbItem);

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
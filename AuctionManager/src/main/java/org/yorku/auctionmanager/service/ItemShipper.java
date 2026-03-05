package org.yorku.auctionmanager.service;

import org.yorku.auctionmanager.dto.ItemShipperResponse;

public interface ItemShipper {
    ItemShipperResponse shipItem(AuthenticatedRequest request);
}

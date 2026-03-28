package org.yorku.auctionmanager.service;

import org.yorku.auctionmanager.dto.*;

public interface ItemShipper {
    ItemShipperResponse shipItem(AuthenticatedRequest request);
}

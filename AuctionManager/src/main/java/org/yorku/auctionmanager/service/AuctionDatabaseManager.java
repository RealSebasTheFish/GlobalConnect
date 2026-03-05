package org.yorku.auctionmanager.service;

import org.yorku.auctionmanager.dto.*;
import org.yorku.auctionmanager.model.*;
import org.yorku.auctionmanager.service.impl.*;
import java.util.List;

public interface AuctionDatabaseManager {
    AuctionDatabaseResponse fetchCatalogue();
    AuctionDatabaseResponse addItem(AuthenticatedRequest request);
    AuctionDatabaseResponse removeItem(AuthenticatedRequest request);
    AuctionDatabaseResponse modifyItem(AuthenticatedRequest request);
    AuctionDatabaseResponse fetchUserItems(AuthenticatedRequest request);
    AuctionDatabaseResponse pushAuctionUpdate(AuctionUpdate auctionUpdate);
}




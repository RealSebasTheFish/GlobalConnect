package org.yorku.auctionmanager.service;

import org.yorku.auctionmanager.dto.*;

public interface AuctionResolver {
    AuctionResolverResponse placeBid(AuthenticatedRequest request);
}

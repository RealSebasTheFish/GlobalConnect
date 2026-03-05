package org.yorku.auctionmanager.service;

import org.yorku.auctionmanager.dto.AuctionResolverResponse;

public interface AuctionResolver {
    AuctionResolverResponse placeBid(AuthenticatedRequest request);
}

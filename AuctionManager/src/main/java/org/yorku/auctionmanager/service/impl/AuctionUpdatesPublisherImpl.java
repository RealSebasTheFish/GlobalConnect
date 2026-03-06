package org.yorku.auctionmanager.service.impl;

import org.springframework.stereotype.Service;
import org.yorku.auctionmanager.service.AuctionUpdatesPublisher;

@Service
public class AuctionUpdatesPublisherImpl implements AuctionUpdatesPublisher {

    @Override
    public void receiveShipUpdate() {
        System.out.println("MOCK PUBLISHER: Pushing shipping update to Gateway...");
    }

    @Override
    public void systemTick() {
        System.out.println("MOCK PUBLISHER: System Tick (Checking for expired auctions)...");
    }

    @Override
    public void receiveBidUpdate() {
        System.out.println("MOCK PUBLISHER: Pushing bid update to Gateway...");
    }
}
package org.yorku.auctionmanager.service;

public interface AuctionUpdatesPublisher {
    void systemTick();
    void receiveShipUpdate();
    void receiveBidUpdate();
}

package org.yorku.auctionmanager.service.impl;

import org.springframework.stereotype.Service;
import org.yorku.auctionmanager.service.AuctionUpdatesPublisher;
import org.yorku.auctionmanager.model.AuctionUpdate;

// The @Service annotation is the magic word that fixes your error!
@Service
public class AuctionUpdatesPublisherImpl implements AuctionUpdatesPublisher {

    

    @Override
    public void receiveShipUpdate() {
        System.out.println("MOCK PUBLISHER: Pushing shipping update to system...");
    }

	@Override
	public void systemTick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveBidUpdate() {
		// TODO Auto-generated method stub
		
	}
}
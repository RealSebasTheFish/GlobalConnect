package org.yorku.auctionmanager.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.yorku.auctionmanager.service.AuctionUpdatesPublisher;
import org.yorku.auctionmanager.repository.SqliteAuctionDAO;
import org.yorku.auctionmanager.model.Item;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AuctionUpdatesPublisherImpl implements AuctionUpdatesPublisher {

    private final SqliteAuctionDAO auctionDAO;
    private final RestTemplate restTemplate;
    
    // Hardcoded payment URL matching AuctionDatabaseManagerImpl
    private final String PAYMENT_SERVICE_URL = "http://localhost:8083/api/payment/register-pending";

    public AuctionUpdatesPublisherImpl(SqliteAuctionDAO auctionDAO) {
        this.auctionDAO = auctionDAO;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void receiveShipUpdate() {
        System.out.println("MOCK PUBLISHER: Pushing shipping update to Gateway...");
    }

    @Scheduled(fixedRate = 10000) // Checks every 10 seconds
    @Override
    public void systemTick() {
        System.out.println("SYSTEM TICK: Checking for expired auctions...");
        
        List<Item> openItems = auctionDAO.fetchAllItems(); // Returns only open items
        long currentTime = System.currentTimeMillis();

        for (Item item : openItems) {
            if (item.getAuctionEndTime() > 0 && currentTime >= item.getAuctionEndTime()) {
                System.out.println("Auction ended for item: " + item.getName() + " (ID: " + item.getId() + ")");
                
                // Close the item
                item.setClosed(true);
                auctionDAO.updateItem(item);
                
                // Notify payment service if there's a winner
                if (item.getHighestBidderUid() > 0 && item.getCurrentHighestBid() > item.getStartingPrice() || item.getHighestBidderUid() > 0) {
                     notifyPaymentService(item);
                }
            }
        }
    }

    private void notifyPaymentService(Item item) {
        try {
            java.util.Map<String, Object> payRequestData = new java.util.HashMap<>();
            payRequestData.put("requestType", "PayRequest");
            payRequestData.put("accountUID", item.getHighestBidderUid());
            payRequestData.put("itemId", item.getId());

            java.util.Map<String, Object> wrapper = new java.util.HashMap<>();
            wrapper.put("request", payRequestData);
            
            restTemplate.postForObject(PAYMENT_SERVICE_URL, wrapper, Object.class);
            System.out.println("SUCCESS: Pending payment registered for Item " + item.getId());
        } catch (Exception e) {
            System.err.println("Connection Error notifying Payment Service: " + e.getMessage());
        }
    }

    @Override
    public void receiveBidUpdate() {
        System.out.println("MOCK PUBLISHER: Pushing bid update to Gateway...");
    }
}
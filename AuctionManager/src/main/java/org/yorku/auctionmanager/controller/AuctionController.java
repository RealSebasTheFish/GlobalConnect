package org.yorku.auctionmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yorku.auctionmanager.service.*;
import org.yorku.auctionmanager.dto.*;

@RestController
public class AuctionController {

    //Suggested controller implementation:
	/*@Autowired
    private AuctionDatabaseManager auctionDatabaseManager;

    @Autowired
    private ItemShipper itemShipper;

    @Autowired
    private AuctionResolver auctionResolver;

    @GetMapping("/fetchcatalogue")
    public AuctionDatabaseResponse fetchCatalogue() {
        return auctionDatabaseManager.fetchCatalogue();
    }

    @PostMapping("/additem")
    public AuctionDatabaseResponse addItem(@RequestBody AuthenticatedRequest request) {
        return auctionDatabaseManager.addItem(request);
    }

    @DeleteMapping("/removeitem")
    public AuctionDatabaseResponse removeItem(@RequestBody AuthenticatedRequest request) {
        return auctionDatabaseManager.removeItem(request);
    }

    @PutMapping("/modifyitem")
    public AuctionDatabaseResponse modifyItem(@RequestBody AuthenticatedRequest request) {
        return auctionDatabaseManager.modifyItem(request);
    }

    @PostMapping("/fetchitems")
    public AuctionDatabaseResponse fetchUserItems(@RequestBody AuthenticatedRequest request) {
        return auctionDatabaseManager.fetchUserItems(request);
    }

    @PostMapping("/ship")
    public ItemShipperResponse shipItem(@RequestBody AuthenticatedRequest request) {
        return itemShipper.shipItem(request);
    }

    @PostMapping("/bid")
    public AuctionResolverResponse placeBid(@RequestBody AuthenticatedRequest request) {
        return auctionResolver.placeBid(request);
    }*/
}
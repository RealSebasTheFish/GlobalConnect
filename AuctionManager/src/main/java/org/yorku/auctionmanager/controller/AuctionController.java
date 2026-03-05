package org.yorku.auctionmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yorku.auctionmanager.dto.*;
import org.yorku.auctionmanager.service.AuctionDatabaseManager;
import org.yorku.auctionmanager.service.AuctionResolver;
import org.yorku.auctionmanager.service.ItemShipper;

//Provisional Implementation to test curl endpoints 

@RestController
@RequestMapping("/api/auction")
public class AuctionController {

    @Autowired
    private AuctionDatabaseManager dbManager;

    @Autowired
    private AuctionResolver auctionResolver;

    @Autowired
    private ItemShipper itemShipper;

    // GET /api/auction/catalogue
    @GetMapping("/catalogue")
    public AuctionDatabaseResponse getCatalogue() {
        return dbManager.fetchCatalogue();
    }

    // POST /api/auction/item
    @PostMapping("/item")
    public AuctionDatabaseResponse addItem(@RequestBody AuthenticatedRequest request) {
        return dbManager.addItem(request);
    }

    // PUT /api/auction/item
    @PutMapping("/item")
    public AuctionDatabaseResponse modifyItem(@RequestBody AuthenticatedRequest request) {
        return dbManager.modifyItem(request);
    }

    // DELETE /api/auction/item
    @DeleteMapping("/item")
    public AuctionDatabaseResponse removeItem(@RequestBody AuthenticatedRequest request) {
        return dbManager.removeItem(request);
    }

    // POST /api/auction/bid
    @PostMapping("/bid")
    public AuctionResolverResponse placeBid(@RequestBody AuthenticatedRequest request) {
        return auctionResolver.placeBid(request);
    }

    // POST /api/auction/ship
    @PostMapping("/ship")
    public ItemShipperResponse shipItem(@RequestBody AuthenticatedRequest request) {
        return itemShipper.shipItem(request);
    }
}
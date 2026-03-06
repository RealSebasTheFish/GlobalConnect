package org.yorku.auctionmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yorku.auctionmanager.dto.*;
import org.yorku.auctionmanager.service.AuctionDatabaseManager;
import org.yorku.auctionmanager.service.AuctionResolver;
import org.yorku.auctionmanager.service.ItemShipper;

@RestController
@RequestMapping("/api/auction")
public class AuctionController {

    private final AuctionDatabaseManager dbManager;
    private final AuctionResolver auctionResolver;
    private final ItemShipper itemShipper;

    // Constructor Injection (Replacing @Autowired fields)
    public AuctionController(AuctionDatabaseManager dbManager, AuctionResolver auctionResolver, ItemShipper itemShipper) {
        this.dbManager = dbManager;
        this.auctionResolver = auctionResolver;
        this.itemShipper = itemShipper;
    }

    @GetMapping("/catalogue")
    public ResponseEntity<AuctionDatabaseResponse> getCatalogue() {
        AuctionDatabaseResponse resp = dbManager.fetchCatalogue();
        
        HttpStatus status = (resp.getErrorCode() == 0) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(resp, status);
    }

    @PostMapping("/item")
    public ResponseEntity<AuctionDatabaseResponse> addItem(@RequestBody AuthenticatedRequest request) {
        AuctionDatabaseResponse resp = dbManager.addItem(request);

        HttpStatus status = 
            (resp.getErrorCode() == 0) ? HttpStatus.CREATED :
            (resp.getErrorCode() == 10) ? HttpStatus.BAD_REQUEST :
            HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(resp, status);
    }

    @PutMapping("/item")
    public ResponseEntity<AuctionDatabaseResponse> modifyItem(@RequestBody AuthenticatedRequest request) {
        AuctionDatabaseResponse resp = dbManager.modifyItem(request);

        HttpStatus status = 
            (resp.getErrorCode() == 0) ? HttpStatus.OK :
            (resp.getErrorCode() == 10) ? HttpStatus.BAD_REQUEST :
            (resp.getErrorCode() == 11) ? HttpStatus.NOT_FOUND :
            (resp.getErrorCode() == 16) ? HttpStatus.FORBIDDEN :
            HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(resp, status);
    }

    @DeleteMapping("/item")
    public ResponseEntity<AuctionDatabaseResponse> removeItem(@RequestBody AuthenticatedRequest request) {
        AuctionDatabaseResponse resp = dbManager.removeItem(request);

        HttpStatus status = 
            (resp.getErrorCode() == 0) ? HttpStatus.OK :
            (resp.getErrorCode() == 10) ? HttpStatus.BAD_REQUEST :
            (resp.getErrorCode() == 11) ? HttpStatus.NOT_FOUND :
            (resp.getErrorCode() == 16) ? HttpStatus.FORBIDDEN :
            HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(resp, status);
    }

    @PostMapping("/bid")
    public ResponseEntity<AuctionResolverResponse> placeBid(@RequestBody AuthenticatedRequest request) {
        AuctionResolverResponse resp = auctionResolver.placeBid(request);

        HttpStatus status = 
            (resp.getErrorCode() == 0) ? HttpStatus.OK :
            (resp.getErrorCode() == 10) ? HttpStatus.BAD_REQUEST :
            (resp.getErrorCode() == 11) ? HttpStatus.NOT_FOUND :
            (resp.getErrorCode() == 14) ? HttpStatus.CONFLICT : // Bid too low
            (resp.getErrorCode() == 15) ? HttpStatus.GONE : // Auction ended
            HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(resp, status);
    }

    @PostMapping("/ship")
    public ResponseEntity<ItemShipperResponse> shipItem(@RequestBody AuthenticatedRequest request) {
        ItemShipperResponse resp = itemShipper.shipItem(request);

        HttpStatus status = 
            (resp.getErrorCode() == 0) ? HttpStatus.OK :
            (resp.getErrorCode() == 10) ? HttpStatus.BAD_REQUEST :
            (resp.getErrorCode() == 11) ? HttpStatus.NOT_FOUND :
            (resp.getErrorCode() == 12) ? HttpStatus.PAYMENT_REQUIRED : // Item not paid
            (resp.getErrorCode() == 13) ? HttpStatus.BAD_REQUEST : // Invalid address
            (resp.getErrorCode() == 16) ? HttpStatus.FORBIDDEN :
            HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(resp, status);
    }
}
package org.yorku.gatewaymanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class GatewayController {

    @PostMapping("/signup")
    public void signUp(@RequestBody Object book) {
        return;
    }
    
    @PostMapping("/login")
    public void login(@RequestBody Object book) {
        return;
    }
    
    @PostMapping("/forgotpassword")
    public void forgotPassword(@RequestBody Object book) {
        return;
    }
    
    @GetMapping("/fetchcatalogue")
    public void fetchCatalogue(@RequestBody Object book) {
        return;
    }
    
    @PostMapping("/bid")
    public void bid(@RequestBody Object book) {
        return;
    }
    
    @GetMapping("/authenticate")
    public void authenticate(@RequestBody Object book) {
        return;
    }
    
    @PostMapping("/pay")
    public void pay(@RequestBody Object book) {
        return;
    }
    
    @PostMapping("/additem")
    public void addItem(@RequestBody Object book) {
        return;
    }
    
    @DeleteMapping("/removeitem")
    public void removeItem(@RequestBody Object book) {
        return;
    }
    
    @PutMapping("/modifyitem")
    public void modifyItem(@RequestBody Object book) {
        return;
    }
    
    @GetMapping("/getitem")
    public void getItem(@RequestBody Object book) {
        return;
    }
    
    @GetMapping("/fetchreceipts")
    public void fetchReceipt(@RequestBody Object book) {
        return;
    }
    
    @GetMapping("/fetchaccount")
    public void fetchAccount(@RequestBody Object book) {
        return;
    }
    
    @GetMapping("/fetchitems")
    public void fetchItems(@RequestBody Object book) {
        return;
    }
    
    @GetMapping("/fetchpendingpayments")
    public void fetchPendingPayments(@RequestBody Object book) {
        return;
    }
}
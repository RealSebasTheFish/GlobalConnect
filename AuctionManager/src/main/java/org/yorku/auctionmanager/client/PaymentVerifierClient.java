package org.yorku.auctionmanager.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.List;

@Component
public class PaymentVerifierClient {

    private final RestTemplate restTemplate;
    
    // In a real app, this would be in application.properties (e.g., http://localhost:8082)
    private final String PAYMENT_SERVICE_URL = "http://localhost:8082/api/payment/fetchreceipts";

    public PaymentVerifierClient() {
        this.restTemplate = new RestTemplate();
    }

    public boolean verifyPayment(int itemId, int buyerUid) {
        try {
            // 1. Construct the payload expected by PaymentController (AuthenticatedRequest)
            // We use Maps here for simplicity so you don't have to duplicate the exact DTOs
            Map<String, Object> fetchReceiptsReq = Map.of(
                "accountUID", buyerUid // The buyer we are checking
            );
            
            Map<String, Object> authRequest = Map.of(
                "request", fetchReceiptsReq,
                "accountUID", buyerUid
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(authRequest, headers);

            // 2. Make the HTTP POST call to the Payment microservice
            ResponseEntity<Map> response = restTemplate.postForEntity(
                PAYMENT_SERVICE_URL, 
                requestEntity, 
                Map.class
            );

            // 3. Parse the PaymentDatabaseResponse to see if the receipt exists
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> receipts = (List<Map<String, Object>>) response.getBody().get("receipts");
                
                if (receipts != null) {
                    for (Map<String, Object> receipt : receipts) {
                        // If we find a receipt matching our item ID, the buyer has paid!
                        if ((Integer) receipt.get("itemId") == itemId) {
                            return true; 
                        }
                    }
                }
            }
            return false;

        } catch (Exception e) {
            System.out.println("Error calling Payment Service: " + e.getMessage());
            return false; // Fail secure: if we can't reach the payment service, assume not paid.
        }
    }
}
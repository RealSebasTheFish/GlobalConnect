package org.yorku.gatewaymanager.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.yorku.gatewaymanager.dto.call.additem.AddItemCallRequest;
import org.yorku.gatewaymanager.dto.call.additem.AddItemCallResponse;
import org.yorku.gatewaymanager.dto.call.authenticate.AuthenticateCallRequest;
import org.yorku.gatewaymanager.dto.call.authenticate.AuthenticateCallResponse;
import org.yorku.gatewaymanager.dto.call.bid.BidCallRequest;
import org.yorku.gatewaymanager.dto.call.bid.BidCallResponse;
import org.yorku.gatewaymanager.dto.call.fetchaccount.FetchAccountCallRequest;
import org.yorku.gatewaymanager.dto.call.fetchaccount.FetchAccountCallResponse;
import org.yorku.gatewaymanager.dto.call.fetchcatalogue.FetchCatalogueCallRequest;
import org.yorku.gatewaymanager.dto.call.fetchcatalogue.FetchCatalogueCallResponse;
import org.yorku.gatewaymanager.dto.call.fetchitems.FetchItemsCallRequest;
import org.yorku.gatewaymanager.dto.call.fetchitems.FetchItemsCallResponse;
import org.yorku.gatewaymanager.dto.call.fetchpendingpayments.FetchPendingPaymentsCallRequest;
import org.yorku.gatewaymanager.dto.call.fetchpendingpayments.FetchPendingPaymentsCallResponse;
import org.yorku.gatewaymanager.dto.call.fetchreceipts.FetchReceiptsCallRequest;
import org.yorku.gatewaymanager.dto.call.fetchreceipts.FetchReceiptsCallResponse;
import org.yorku.gatewaymanager.dto.call.login.LoginCallRequest;
import org.yorku.gatewaymanager.dto.call.login.LoginCallResponse;
import org.yorku.gatewaymanager.dto.call.modifyitem.ModifyItemCallRequest;
import org.yorku.gatewaymanager.dto.call.modifyitem.ModifyItemCallResponse;
import org.yorku.gatewaymanager.dto.call.pay.PayCallRequest;
import org.yorku.gatewaymanager.dto.call.pay.PayCallResponse;
import org.yorku.gatewaymanager.dto.call.removeitem.RemoveItemCallRequest;
import org.yorku.gatewaymanager.dto.call.removeitem.RemoveItemCallResponse;
import org.yorku.gatewaymanager.dto.call.resetpassword.ResetPasswordCallRequest;
import org.yorku.gatewaymanager.dto.call.resetpassword.ResetPasswordCallResponse;
import org.yorku.gatewaymanager.dto.call.resetpassword.ResetPasswordStartCallRequest;
import org.yorku.gatewaymanager.dto.call.resetpassword.ResetPasswordStartCallResponse;
import org.yorku.gatewaymanager.dto.call.ship.ShipCallRequest;
import org.yorku.gatewaymanager.dto.call.ship.ShipCallResponse;
import org.yorku.gatewaymanager.dto.call.signup.SignUpCallRequest;
import org.yorku.gatewaymanager.dto.call.signup.SignUpCallResponse;
import org.yorku.gatewaymanager.dto.common.Account;
import org.yorku.gatewaymanager.dto.common.Address;
import org.yorku.gatewaymanager.dto.common.Item;
import org.yorku.gatewaymanager.dto.common.PendingPayment;
import org.yorku.gatewaymanager.dto.common.Receipt;
import org.yorku.gatewaymanager.dto.common.Request;
import org.yorku.gatewaymanager.dto.common.Response;
import org.yorku.gatewaymanager.dto.micro.account.AccountServiceAccountDatabaseResponse;
import org.yorku.gatewaymanager.dto.micro.account.AccountServiceAuthenticationResponse;
import org.yorku.gatewaymanager.dto.micro.account.AccountServiceSessionResponse;
import org.yorku.gatewaymanager.dto.micro.account.fetchaccount.AccountServiceFetchAccountRequest;
import org.yorku.gatewaymanager.dto.micro.account.login.AccountServiceLoginRequest;
import org.yorku.gatewaymanager.dto.micro.account.resetpassword.AccountServiceResetPasswordRequest;
import org.yorku.gatewaymanager.dto.micro.account.resetpasswordstart.AccountServiceResetPasswordStartRequest;
import org.yorku.gatewaymanager.dto.micro.account.signup.AccountServiceSignUpRequest;
import org.yorku.gatewaymanager.dto.micro.auction.AuctionServiceAuthenticatedRequest;
import org.yorku.gatewaymanager.dto.micro.auction.AuctionServiceAuctionDatabaseResponse;
import org.yorku.gatewaymanager.dto.micro.auction.AuctionServiceAuctionResolverResponse;
import org.yorku.gatewaymanager.dto.micro.auction.AuctionServiceItemShipperResponse;
import org.yorku.gatewaymanager.dto.micro.auction.additem.AuctionServiceAddItemRequest;
import org.yorku.gatewaymanager.dto.micro.auction.bid.AuctionServiceBidRequest;
import org.yorku.gatewaymanager.dto.micro.auction.modifyitem.AuctionServiceModifyItemRequest;
import org.yorku.gatewaymanager.dto.micro.auction.removeitem.AuctionServiceRemoveItemRequest;
import org.yorku.gatewaymanager.dto.micro.auction.ship.AuctionServiceShipRequest;
import org.yorku.gatewaymanager.dto.micro.payment.PaymentServiceAuthenticatedRequest;
import org.yorku.gatewaymanager.dto.micro.payment.PaymentServicePaymentDatabaseResponse;
import org.yorku.gatewaymanager.dto.micro.payment.PaymentServicePaymentResponse;
import org.yorku.gatewaymanager.dto.micro.payment.fetchpendingpayments.PaymentServiceFetchPendingPaymentsRequest;
import org.yorku.gatewaymanager.dto.micro.payment.fetchreceipts.PaymentServiceFetchReceiptsRequest;
import org.yorku.gatewaymanager.dto.micro.payment.pay.PaymentServicePayRequest;

@RestController
@RequestMapping("/")
public class GatewayController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.account.base-url:http://localhost:8081}")
    private String accountBaseUrl;

    @Value("${services.auction.base-url:http://localhost:8082}")
    private String auctionBaseUrl;

    @Value("${services.payment.base-url:http://localhost:8083}")
    private String paymentBaseUrl;

    @PostMapping("/signup")
    public SignUpCallResponse signUp(@RequestBody SignUpCallRequest request) {
        SignUpCallResponse out = new SignUpCallResponse();

        AccountServiceSignUpRequest microRequest = new AccountServiceSignUpRequest();
        microRequest.setUsername(request.getUsername());
        microRequest.setPassword(request.getPassword());
        microRequest.setEmail(request.getEmail());
        microRequest.setFirstName(request.getFirstName());
        microRequest.setLastName(request.getLastName());
        if (request.getAddress() != null) {
            microRequest.setStreetName(request.getAddress().getStreetName());
            microRequest.setStreetNumber(request.getAddress().getStreetNumber());
            microRequest.setCity(request.getAddress().getCity());
            microRequest.setCountry(request.getAddress().getCountry());
            microRequest.setPostalCode(request.getAddress().getPostalCode());
        }

        AccountServiceAccountDatabaseResponse signupResp = restTemplate.postForObject(
            accountBaseUrl + "/signup",
            microRequest,
            AccountServiceAccountDatabaseResponse.class
        );

        if (signupResp == null) {
            return error(out, 2, "Account service unavailable.");
        }

        out.setErrorCode(signupResp.getErrorCode());
        out.setMessage(defaultMessage(signupResp.getErrorCode(), "Sign up failed."));

        if (signupResp.getErrorCode() != 0) {
            return out;
        }

        if (signupResp.getAccounts() != null && !signupResp.getAccounts().isEmpty()) {
            Account created = signupResp.getAccounts().get(0);
            out.setAccount(created);

            AccountServiceLoginRequest loginRequest = new AccountServiceLoginRequest();
            loginRequest.setAccountUID(created.getAccountUID());
            loginRequest.setPassword(request.getPassword());

            AccountServiceSessionResponse sessionResp = restTemplate.postForObject(
                accountBaseUrl + "/login",
                loginRequest,
                AccountServiceSessionResponse.class
            );

            if (sessionResp != null && sessionResp.getErrorCode() == 0) {
                out.setSessionToken(sessionResp.getSessionToken());
                out.setMessage("Sign up successful.");
            }
        }

        return out;
    }

    @PostMapping("/login")
    public LoginCallResponse login(@RequestBody LoginCallRequest request) {
        LoginCallResponse out = new LoginCallResponse();

        AccountServiceLoginRequest microRequest = new AccountServiceLoginRequest();
        microRequest.setAccountUID(request.getAccountUID());
        microRequest.setPassword(request.getPassword());

        AccountServiceSessionResponse loginResp = restTemplate.postForObject(
            accountBaseUrl + "/login",
            microRequest,
            AccountServiceSessionResponse.class
        );

        if (loginResp == null) {
            return error(out, 2, "Account service unavailable.");
        }

        out.setErrorCode(loginResp.getErrorCode());
        out.setSessionToken(loginResp.getSessionToken());
        out.setMessage(defaultMessage(loginResp.getErrorCode(), "Login failed."));
        return out;
    }

    @PostMapping("/authenticate")
    public AuthenticateCallResponse authenticate(@RequestBody AuthenticateCallRequest request) {
        AuthenticateCallResponse out = new AuthenticateCallResponse();

        Integer accountUID = extractAccountUID(request.getRequest());
        if (accountUID == null) {
            return error(out, 6, "Authenticated requests currently need an accountUID field in the embedded request.");
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (authResp == null) {
            return error(out, 2, "Account service unavailable.");
        }

        out.setErrorCode(authResp.getErrorCode());
        out.setMessage(defaultMessage(authResp.getErrorCode(), "Authentication failed."));

        if (authResp.getErrorCode() == 0 && authResp.getAuthenticatedRequest() != null) {
            out.setSecret(authResp.getAuthenticatedRequest().getSecret());
            out.setRequest(request.getRequest());
            out.setAccountUID(accountUID);
        }

        return out;
    }

    @PostMapping("/resetpassword/request")
    public ResetPasswordStartCallResponse resetPasswordRequest(@RequestBody ResetPasswordStartCallRequest request) {
        ResetPasswordStartCallResponse out = new ResetPasswordStartCallResponse();

        AccountServiceResetPasswordStartRequest microRequest = new AccountServiceResetPasswordStartRequest();
        microRequest.setAccountUID(request.getAccountUID());

        Map<?, ?> resp = restTemplate.postForObject(
            accountBaseUrl + "/forgotpassword/request",
            microRequest,
            Map.class
        );

        if (resp == null) {
            return error(out, 2, "Account service unavailable.");
        }

        Object code = resp.get("forgotPasswordRescueCode");
        Object errorCode = resp.get("errorCode");
        out.setErrorCode(asInt(errorCode, 2));
        out.setForgotPasswordRescueCode(code == null ? null : code.toString());
        out.setMessage(defaultMessage(out.getErrorCode(), "Reset password request failed."));
        return out;
    }

    @PostMapping("/resetpassword")
    public ResetPasswordCallResponse resetPassword(@RequestBody ResetPasswordCallRequest request) {
        ResetPasswordCallResponse out = new ResetPasswordCallResponse();

        AccountServiceResetPasswordRequest microRequest = new AccountServiceResetPasswordRequest();
        microRequest.setAccountUID(request.getAccountUID());
        microRequest.setForgotPasswordRescueCode(request.getForgotPasswordRescueCode());
        microRequest.setNewPassword(request.getNewPassword());

        AccountServiceAccountDatabaseResponse resp = restTemplate.postForObject(
            accountBaseUrl + "/forgotpassword",
            microRequest,
            AccountServiceAccountDatabaseResponse.class
        );

        if (resp == null) {
            return error(out, 2, "Account service unavailable.");
        }

        out.setErrorCode(resp.getErrorCode());
        out.setMessage(defaultMessage(resp.getErrorCode(), "Reset password failed."));
        return out;
    }

    @PostMapping("/fetchcatalogue")
    public FetchCatalogueCallResponse fetchCatalogue(@RequestBody(required = false) FetchCatalogueCallRequest request) {
        FetchCatalogueCallResponse out = new FetchCatalogueCallResponse();

        ResponseEntity<AuctionServiceAuctionDatabaseResponse> resp = restTemplate.exchange(
            auctionBaseUrl + "/api/auction/catalogue",
            HttpMethod.GET,
            HttpEntity.EMPTY,
            AuctionServiceAuctionDatabaseResponse.class
        );

        AuctionServiceAuctionDatabaseResponse body = resp.getBody();
        if (body == null) {
            return error(out, 2, "Auction service unavailable.");
        }

        out.setErrorCode(body.getErrorCode());
        out.setMessage(defaultMessage(body.getErrorCode(), "Fetch catalogue failed."));
        out.setItems(body.getItems());
        return out;
    }

    @PostMapping("/bid")
    public BidCallResponse bid(@RequestBody BidCallRequest request) {
        BidCallResponse out = new BidCallResponse();

        Integer accountUID = extractAccountUID(request);
        if (accountUID == null) {
            return error(out, 6, "Bid requests need accountUID added to the authenticated request base class.");
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return error(out, authErrorCode(authResp), "Authentication failed.");
        }

        AuctionServiceBidRequest inner = new AuctionServiceBidRequest();
        inner.setTargetItemId(request.getTargetItemId());
        inner.setBidAmount(request.getBidAmount());

        AuctionServiceAuthenticatedRequest forward = new AuctionServiceAuthenticatedRequest();
        forward.setSecret(authResp.getAuthenticatedRequest().getSecret());
        forward.setAccountUID(accountUID);
        forward.setRequest(inner);

        AuctionServiceAuctionResolverResponse resp = restTemplate.postForObject(
            auctionBaseUrl + "/api/auction/bid",
            forward,
            AuctionServiceAuctionResolverResponse.class
        );

        if (resp == null) {
            return error(out, 2, "Auction service unavailable.");
        }

        out.setErrorCode(resp.getErrorCode());
        out.setMessage(defaultMessage(resp.getErrorCode(), "Bid failed."));
        return out;
    }

    @PostMapping("/pay")
    public PayCallResponse pay(@RequestBody PayCallRequest request) {
        PayCallResponse out = new PayCallResponse();

        Integer accountUID = extractAccountUID(request);
        if (accountUID == null) {
            return error(out, 6, "Pay requests need accountUID added to the authenticated request base class.");
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return error(out, authErrorCode(authResp), "Authentication failed.");
        }

        PaymentServicePayRequest inner = new PaymentServicePayRequest();
        inner.setAccountUID(accountUID);
        inner.setItemId(request.getItemId());
        inner.setCardNumber(request.getCardNumber());
        inner.setName(request.getName());
        inner.setExpDate(request.getExpDate());
        inner.setSecurityCode(request.getSecurityCode());
        inner.setPaymentMethod(request.getPaymentMethod());
        inner.setAmount(request.getAmount());
        inner.setShippingCost(request.getShippingCost());
        inner.setExpeditedShipping(request.isExpeditedShipping());
        inner.setExpeditedExtraCost(request.getExpeditedExtraCost());

        PaymentServiceAuthenticatedRequest forward = new PaymentServiceAuthenticatedRequest();
        forward.setSecret(authResp.getAuthenticatedRequest().getSecret());
        forward.setRequest(inner);

        PaymentServicePaymentResponse resp = restTemplate.postForObject(
            paymentBaseUrl + "/api/payment/pay",
            forward,
            PaymentServicePaymentResponse.class
        );

        if (resp == null) {
            return error(out, 2, "Payment service unavailable.");
        }

        out.setErrorCode(resp.getErrorCode());
        out.setMessage(defaultMessage(resp.getErrorCode(), "Payment failed."));
        out.setReceipt(resp.getReceipt());
        return out;
    }

    @PostMapping("/additem")
    public AddItemCallResponse addItem(@RequestBody AddItemCallRequest request) {
        AddItemCallResponse out = new AddItemCallResponse();

        Integer accountUID = extractAccountUID(request);
        if (accountUID == null) {
            return error(out, 6, "Add-item requests need accountUID added to the authenticated request base class.");
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return error(out, authErrorCode(authResp), "Authentication failed.");
        }

        if (request.getItem() != null) {
            request.getItem().setOwnerUid(accountUID);
        }

        AuctionServiceAddItemRequest inner = new AuctionServiceAddItemRequest();
        inner.setItem(request.getItem());

        AuctionServiceAuthenticatedRequest forward = new AuctionServiceAuthenticatedRequest();
        forward.setSecret(authResp.getAuthenticatedRequest().getSecret());
        forward.setAccountUID(accountUID);
        forward.setRequest(inner);

        AuctionServiceAuctionDatabaseResponse resp = restTemplate.postForObject(
            auctionBaseUrl + "/api/auction/item",
            forward,
            AuctionServiceAuctionDatabaseResponse.class
        );

        if (resp == null) {
            return error(out, 2, "Auction service unavailable.");
        }

        out.setErrorCode(resp.getErrorCode());
        out.setMessage(defaultMessage(resp.getErrorCode(), "Add item failed."));
        if (resp.getItems() != null && !resp.getItems().isEmpty()) {
            out.setItem(resp.getItems().get(0));
        } else {
            out.setItem(request.getItem());
        }
        return out;
    }

    @DeleteMapping("/removeitem")
    public RemoveItemCallResponse removeItem(@RequestBody RemoveItemCallRequest request) {
        RemoveItemCallResponse out = new RemoveItemCallResponse();

        Integer accountUID = extractAccountUID(request);
        if (accountUID == null) {
            return error(out, 6, "Remove-item requests need accountUID added to the authenticated request base class.");
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return error(out, authErrorCode(authResp), "Authentication failed.");
        }

        AuctionServiceRemoveItemRequest inner = new AuctionServiceRemoveItemRequest();
        inner.setItemId(request.getItemId());

        AuctionServiceAuthenticatedRequest forward = new AuctionServiceAuthenticatedRequest();
        forward.setSecret(authResp.getAuthenticatedRequest().getSecret());
        forward.setAccountUID(accountUID);
        forward.setRequest(inner);

        ResponseEntity<AuctionServiceAuctionDatabaseResponse> entity = restTemplate.exchange(
            auctionBaseUrl + "/api/auction/item",
            HttpMethod.DELETE,
            new HttpEntity<>(forward),
            AuctionServiceAuctionDatabaseResponse.class
        );

        AuctionServiceAuctionDatabaseResponse resp = entity.getBody();
        if (resp == null) {
            return error(out, 2, "Auction service unavailable.");
        }

        out.setErrorCode(resp.getErrorCode());
        out.setMessage(defaultMessage(resp.getErrorCode(), "Remove item failed."));
        return out;
    }

    @PutMapping("/modifyitem")
    public ModifyItemCallResponse modifyItem(@RequestBody ModifyItemCallRequest request) {
        ModifyItemCallResponse out = new ModifyItemCallResponse();

        Integer accountUID = extractAccountUID(request);
        if (accountUID == null) {
            return error(out, 6, "Modify-item requests need accountUID added to the authenticated request base class.");
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return error(out, authErrorCode(authResp), "Authentication failed.");
        }

        if (request.getItem() != null && request.getItem().getOwnerUid() == 0) {
            request.getItem().setOwnerUid(accountUID);
        }

        AuctionServiceModifyItemRequest inner = new AuctionServiceModifyItemRequest();
        inner.setItem(request.getItem());

        AuctionServiceAuthenticatedRequest forward = new AuctionServiceAuthenticatedRequest();
        forward.setSecret(authResp.getAuthenticatedRequest().getSecret());
        forward.setAccountUID(accountUID);
        forward.setRequest(inner);

        ResponseEntity<AuctionServiceAuctionDatabaseResponse> entity = restTemplate.exchange(
            auctionBaseUrl + "/api/auction/item",
            HttpMethod.PUT,
            new HttpEntity<>(forward),
            AuctionServiceAuctionDatabaseResponse.class
        );

        AuctionServiceAuctionDatabaseResponse resp = entity.getBody();
        if (resp == null) {
            return error(out, 2, "Auction service unavailable.");
        }

        out.setErrorCode(resp.getErrorCode());
        out.setMessage(defaultMessage(resp.getErrorCode(), "Modify item failed."));
        if (resp.getItems() != null && !resp.getItems().isEmpty()) {
            out.setItem(resp.getItems().get(0));
        } else {
            out.setItem(request.getItem());
        }
        return out;
    }

    @PostMapping("/ship")
    public ShipCallResponse ship(@RequestBody ShipCallRequest request) {
        ShipCallResponse out = new ShipCallResponse();

        Integer accountUID = extractAccountUID(request);
        if (accountUID == null) {
            return error(out, 6, "Ship requests need accountUID added to the authenticated request base class.");
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return error(out, authErrorCode(authResp), "Authentication failed.");
        }

        AuctionServiceShipRequest inner = new AuctionServiceShipRequest();
        inner.setTargetItem(request.getTargetItem());
        inner.setShippingAddress(request.getShippingAddress());

        AuctionServiceAuthenticatedRequest forward = new AuctionServiceAuthenticatedRequest();
        forward.setSecret(authResp.getAuthenticatedRequest().getSecret());
        forward.setAccountUID(accountUID);
        forward.setRequest(inner);

        AuctionServiceItemShipperResponse resp = restTemplate.postForObject(
            auctionBaseUrl + "/api/auction/ship",
            forward,
            AuctionServiceItemShipperResponse.class
        );

        if (resp == null) {
            return error(out, 2, "Auction service unavailable.");
        }

        out.setErrorCode(resp.getErrorCode());
        out.setMessage(defaultMessage(resp.getErrorCode(), "Ship item failed."));
        return out;
    }

    @PostMapping("/fetchaccount")
    public FetchAccountCallResponse fetchAccount(@RequestBody FetchAccountCallRequest request) {
        FetchAccountCallResponse out = new FetchAccountCallResponse();

        Integer accountUID = extractAccountUID(request);
        if (accountUID == null) {
            return error(out, 6, "Fetch-account requests need accountUID added to the authenticated request base class.");
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return error(out, authErrorCode(authResp), "Authentication failed.");
        }

        AccountServiceFetchAccountRequest forward = new AccountServiceFetchAccountRequest();
        forward.setSecret(authResp.getAuthenticatedRequest().getSecret());
        forward.setRequest(buildAccountLookupRequest(accountUID));

        AccountServiceAccountDatabaseResponse resp = restTemplate.postForObject(
            accountBaseUrl + "/fetchaccount",
            forward,
            AccountServiceAccountDatabaseResponse.class
        );

        if (resp == null) {
            return error(out, 2, "Account service unavailable.");
        }

        out.setErrorCode(resp.getErrorCode());
        out.setMessage(defaultMessage(resp.getErrorCode(), "Fetch account failed."));
        if (resp.getAccounts() != null && !resp.getAccounts().isEmpty()) {
            out.setAccount(resp.getAccounts().get(0));
        }
        return out;
    }

    @PostMapping("/fetchitems")
    public FetchItemsCallResponse fetchItems(@RequestBody FetchItemsCallRequest request) {
        FetchItemsCallResponse out = new FetchItemsCallResponse();

        Integer accountUID = extractAccountUID(request);
        if (accountUID == null) {
            return error(out, 6, "Fetch-items requests need accountUID added to the authenticated request base class.");
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return error(out, authErrorCode(authResp), "Authentication failed.");
        }

        ResponseEntity<AuctionServiceAuctionDatabaseResponse> entity = restTemplate.exchange(
            auctionBaseUrl + "/api/auction/catalogue",
            HttpMethod.GET,
            HttpEntity.EMPTY,
            AuctionServiceAuctionDatabaseResponse.class
        );

        AuctionServiceAuctionDatabaseResponse resp = entity.getBody();
        if (resp == null) {
            return error(out, 2, "Auction service unavailable.");
        }

        List<Item> ownedItems = new ArrayList<>();
        if (resp.getItems() != null) {
            for (Item item : resp.getItems()) {
                if (item != null && item.getOwnerUid() == accountUID) {
                    ownedItems.add(item);
                }
            }
        }

        out.setErrorCode(resp.getErrorCode());
        out.setMessage(defaultMessage(resp.getErrorCode(), "Fetch items failed."));
        out.setItems(ownedItems);
        return out;
    }

    @PostMapping("/fetchreceipts")
    public FetchReceiptsCallResponse fetchReceipts(@RequestBody FetchReceiptsCallRequest request) {
        FetchReceiptsCallResponse out = new FetchReceiptsCallResponse();

        Integer accountUID = extractAccountUID(request);
        if (accountUID == null) {
            return error(out, 6, "Fetch-receipts requests need accountUID added to the authenticated request base class.");
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return error(out, authErrorCode(authResp), "Authentication failed.");
        }

        PaymentServiceFetchReceiptsRequest inner = new PaymentServiceFetchReceiptsRequest();
        inner.setAccountUID(accountUID);

        PaymentServiceAuthenticatedRequest forward = new PaymentServiceAuthenticatedRequest();
        forward.setSecret(authResp.getAuthenticatedRequest().getSecret());
        forward.setRequest(inner);

        PaymentServicePaymentDatabaseResponse resp = restTemplate.postForObject(
            paymentBaseUrl + "/api/payment/fetchreceipts",
            forward,
            PaymentServicePaymentDatabaseResponse.class
        );

        if (resp == null) {
            return error(out, 2, "Payment service unavailable.");
        }

        out.setErrorCode(resp.getErrorCode());
        out.setMessage(defaultMessage(resp.getErrorCode(), "Fetch receipts failed."));
        out.setReceipts(resp.getReceipts());
        return out;
    }

    @PostMapping("/fetchpendingpayments")
    public FetchPendingPaymentsCallResponse fetchPendingPayments(@RequestBody FetchPendingPaymentsCallRequest request) {
        FetchPendingPaymentsCallResponse out = new FetchPendingPaymentsCallResponse();

        Integer accountUID = extractAccountUID(request);
        if (accountUID == null) {
            return error(out, 6, "Fetch-pending-payments requests need accountUID added to the authenticated request base class.");
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return error(out, authErrorCode(authResp), "Authentication failed.");
        }

        PaymentServiceFetchPendingPaymentsRequest inner = new PaymentServiceFetchPendingPaymentsRequest();
        inner.setAccountUID(accountUID);

        PaymentServiceAuthenticatedRequest forward = new PaymentServiceAuthenticatedRequest();
        forward.setSecret(authResp.getAuthenticatedRequest().getSecret());
        forward.setRequest(inner);

        PaymentServicePaymentDatabaseResponse resp = restTemplate.postForObject(
            paymentBaseUrl + "/api/payment/fetchpendingpayments",
            forward,
            PaymentServicePaymentDatabaseResponse.class
        );

        if (resp == null) {
            return error(out, 2, "Payment service unavailable.");
        }

        out.setErrorCode(resp.getErrorCode());
        out.setMessage(defaultMessage(resp.getErrorCode(), "Fetch pending payments failed."));
        out.setPendingPayments(resp.getPendingPayments());
        return out;
    }

    private AccountServiceAuthenticationResponse authenticateWithAccountService(String sessionToken, Integer accountUID) {
        if (sessionToken == null || sessionToken.trim().isEmpty() || accountUID == null) {
            return null;
        }

        Map<String, Object> authRequest = new HashMap<>();
        authRequest.put("sessionToken", sessionToken);
        authRequest.put("request", buildAccountLookupRequest(accountUID));

        return restTemplate.postForObject(
            accountBaseUrl + "/authenticate",
            authRequest,
            AccountServiceAuthenticationResponse.class
        );
    }

    private AccountServiceFetchAccountRequest buildAccountLookupRequest(int accountUID) {
        AccountServiceFetchAccountRequest request = new AccountServiceFetchAccountRequest();
        request.setAccountUID(accountUID);
        return request;
    }

    private boolean isAuthenticated(AccountServiceAuthenticationResponse authResp) {
        return authResp != null && authResp.getErrorCode() == 0 && authResp.getAuthenticatedRequest() != null;
    }

    private int authErrorCode(AccountServiceAuthenticationResponse authResp) {
        return authResp == null ? 2 : authResp.getErrorCode();
    }

    private Integer extractAccountUID(Object request) {
        if (request == null) {
            return null;
        }
        
        String[] methodNames = {"getAccountUID", "getAccountUid"};
        for (String methodName : methodNames) {
            try {
                Method method = request.getClass().getMethod(methodName);
                Object value = method.invoke(request);
                if (value instanceof Integer) {
                    return (Integer) value;
                }
                if (value instanceof Number) {
                    return ((Number) value).intValue();
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private int asInt(Object value, int fallback) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception ex) {
            return fallback;
        }
    }

    private String defaultMessage(int errorCode, String fallback) {
        return errorCode == 0 ? "Success." : fallback;
    }

    private <T extends Response> T error(T response, int errorCode, String message) {
        response.setErrorCode(errorCode);
        response.setMessage(message);
        return response;
    }
}

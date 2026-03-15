package org.yorku.gatewaymanager.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
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
import org.yorku.gatewaymanager.dto.common.Item;
import org.yorku.gatewaymanager.dto.common.Response;
import org.yorku.gatewaymanager.dto.micro.account.AccountServiceAccountDatabaseResponse;
import org.yorku.gatewaymanager.dto.micro.account.AccountServiceAuthenticatedRequest;
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
    public ResponseEntity<SignUpCallResponse> signUp(@RequestBody SignUpCallRequest request) {
        SignUpCallResponse out = new SignUpCallResponse();

        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword()) || isBlank(request.getEmail())) {
            return badRequest(error(out, 1, "Missing required sign-up fields."));
        }

        try {
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
                return downstreamError(error(out, 2, "Account service unavailable."));
            }

            out.setErrorCode(signupResp.getErrorCode());
            out.setMessage(defaultMessage(signupResp.getErrorCode(), "Sign up failed."));

            if (signupResp.getErrorCode() != 0) {
                return conflict(out);
            }

            if (signupResp.getAccounts() != null && !signupResp.getAccounts().isEmpty()) {
                Account created = signupResp.getAccounts().get(0);
                out.setAccount(created);

                AccountServiceLoginRequest loginRequest = new AccountServiceLoginRequest();
                loginRequest.setAccountUID(created.getAccountUID());
                loginRequest.setPassword(request.getPassword());

                try {
                    AccountServiceSessionResponse sessionResp = restTemplate.postForObject(
                        accountBaseUrl + "/login",
                        loginRequest,
                        AccountServiceSessionResponse.class
                    );

                    if (sessionResp != null && sessionResp.getErrorCode() == 0) {
                        out.setSessionToken(sessionResp.getSessionToken());
                        out.setMessage("Sign up successful.");
                    }
                } catch (RestClientException ignored) {
                }
            }

            return created(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Sign up failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Account service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Account service request failed."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginCallResponse> login(@RequestBody LoginCallRequest request) {
        LoginCallResponse out = new LoginCallResponse();

        if (request == null || request.getAccountUID() <= 0 || isBlank(request.getPassword())) {
            return badRequest(error(out, 1, "Missing accountUID or password."));
        }

        try {
            AccountServiceLoginRequest microRequest = new AccountServiceLoginRequest();
            microRequest.setAccountUID(request.getAccountUID());
            microRequest.setPassword(request.getPassword());

            AccountServiceSessionResponse loginResp = restTemplate.postForObject(
                accountBaseUrl + "/login",
                microRequest,
                AccountServiceSessionResponse.class
            );

            if (loginResp == null) {
                return downstreamError(error(out, 2, "Account service unavailable."));
            }

            out.setErrorCode(loginResp.getErrorCode());
            out.setSessionToken(loginResp.getSessionToken());
            out.setMessage(defaultMessage(loginResp.getErrorCode(), "Login failed."));

            return loginResp.getErrorCode() == 0 ? ok(out) : unauthorized(out);
        } catch (HttpStatusCodeException ex) {
            return unauthorized(error(out, 2, "Login failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Account service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Account service request failed."));
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateCallResponse> authenticate(@RequestBody AuthenticateCallRequest request) {
        AuthenticateCallResponse out = new AuthenticateCallResponse();

        Integer accountUID = request != null && request.getRequest() != null
            ? request.getRequest().getAccountUID()
            : null;

        if (request == null || isBlank(request.getSessionToken()) || accountUID == null || accountUID <= 0) {
            return badRequest(error(out, 1, "Authenticate requires sessionToken and embedded request.accountUID."));
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (authResp == null) {
            return unauthorized(error(out, 2, "Authentication failed."));
        }

        out.setErrorCode(authResp.getErrorCode());
        out.setMessage(defaultMessage(authResp.getErrorCode(), "Authentication failed."));

        if (authResp.getErrorCode() == 0 && authResp.getAuthenticatedRequest() != null) {
            out.setSecret(authResp.getAuthenticatedRequest().getSecret());
            out.setAccountUID(accountUID);
            return ok(out);
        }

        return unauthorized(out);
    }

    @PostMapping("/resetpassword/request")
    public ResponseEntity<ResetPasswordStartCallResponse> resetPasswordRequest(@RequestBody ResetPasswordStartCallRequest request) {
        ResetPasswordStartCallResponse out = new ResetPasswordStartCallResponse();

        if (request == null || request.getAccountUID() <= 0) {
            return badRequest(error(out, 1, "Reset-password request requires accountUID."));
        }

        try {
            AccountServiceResetPasswordStartRequest microRequest = new AccountServiceResetPasswordStartRequest();
            microRequest.setAccountUID(request.getAccountUID());

            @SuppressWarnings("unchecked")
            Map<String, Object> resp = restTemplate.postForObject(
                accountBaseUrl + "/forgotpassword/request",
                microRequest,
                Map.class
            );

            if (resp == null) {
                return downstreamError(error(out, 2, "Account service unavailable."));
            }

            Object code = resp.get("forgotPasswordRescueCode");
            Object errorCode = resp.get("errorCode");
            out.setErrorCode(asInt(errorCode, 2));
            out.setForgotPasswordRescueCode(code == null ? null : code.toString());
            out.setMessage(defaultMessage(out.getErrorCode(), "Reset password request failed."));

            return out.getErrorCode() == 0 ? ok(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Reset password request failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Account service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Account service request failed."));
        }
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<ResetPasswordCallResponse> resetPassword(@RequestBody ResetPasswordCallRequest request) {
        ResetPasswordCallResponse out = new ResetPasswordCallResponse();

        if (request == null || request.getAccountUID() <= 0 || isBlank(request.getForgotPasswordRescueCode()) || isBlank(request.getNewPassword())) {
            return badRequest(error(out, 1, "Reset-password requires accountUID, rescue code, and newPassword."));
        }

        try {
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
                return downstreamError(error(out, 2, "Account service unavailable."));
            }

            out.setErrorCode(resp.getErrorCode());
            out.setMessage(defaultMessage(resp.getErrorCode(), "Reset password failed."));
            return resp.getErrorCode() == 0 ? ok(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Reset password failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Account service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Account service request failed."));
        }
    }

    @PostMapping("/fetchcatalogue")
    public ResponseEntity<FetchCatalogueCallResponse> fetchCatalogue(@RequestBody(required = false) FetchCatalogueCallRequest request) {
        FetchCatalogueCallResponse out = new FetchCatalogueCallResponse();

        try {
            ResponseEntity<AuctionServiceAuctionDatabaseResponse> resp = restTemplate.exchange(
                auctionBaseUrl + "/api/auction/catalogue",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                AuctionServiceAuctionDatabaseResponse.class
            );

            AuctionServiceAuctionDatabaseResponse body = resp.getBody();
            if (body == null) {
                return downstreamError(error(out, 2, "Auction service unavailable."));
            }

            out.setErrorCode(body.getErrorCode());
            out.setMessage(defaultMessage(body.getErrorCode(), "Fetch catalogue failed."));
            out.setItems(body.getItems());

            return body.getErrorCode() == 0 ? ok(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Fetch catalogue failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Auction service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Auction service request failed."));
        }
    }

    @PostMapping("/bid")
    public ResponseEntity<BidCallResponse> bid(@RequestBody BidCallRequest request) {
        BidCallResponse out = new BidCallResponse();

        Integer accountUID = extractAccountUID(request);
        ResponseEntity<BidCallResponse> authValidation = validateAuthenticatedCall(request == null ? null : request.getSessionToken(), accountUID, out);
        if (authValidation != null) {
            return authValidation;
        }

        if (request.getTargetItemId() <= 0 || request.getBidAmount() <= 0) {
            return badRequest(error(out, 1, "Bid requires valid targetItemId and bidAmount."));
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return unauthorized(error(out, authErrorCode(authResp), "Authentication failed."));
        }

        try {
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
                return downstreamError(error(out, 2, "Auction service unavailable."));
            }

            out.setErrorCode(resp.getErrorCode());
            out.setMessage(defaultMessage(resp.getErrorCode(), "Bid failed."));
            return resp.getErrorCode() == 0 ? ok(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Bid failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Auction service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Auction service request failed."));
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<PayCallResponse> pay(@RequestBody PayCallRequest request) {
        PayCallResponse out = new PayCallResponse();

        Integer accountUID = extractAccountUID(request);
        ResponseEntity<PayCallResponse> authValidation = validateAuthenticatedCall(request == null ? null : request.getSessionToken(), accountUID, out);
        if (authValidation != null) {
            return authValidation;
        }

        if (request.getItemId() <= 0 || isBlank(request.getCardNumber()) || isBlank(request.getName())
                || isBlank(request.getExpDate()) || isBlank(request.getSecurityCode())
                || isBlank(request.getPaymentMethod()) || request.getAmount() <= 0) {
            return badRequest(error(out, 1, "Pay requires itemId, card fields, payment method, and positive amount."));
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return unauthorized(error(out, authErrorCode(authResp), "Authentication failed."));
        }

        try {
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
                return downstreamError(error(out, 2, "Payment service unavailable."));
            }

            out.setErrorCode(resp.getErrorCode());
            out.setMessage(defaultMessage(resp.getErrorCode(), "Payment failed."));
            out.setReceipt(resp.getReceipt());
            return resp.getErrorCode() == 0 ? ok(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Payment failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Payment service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Payment service request failed."));
        }
    }

    @PostMapping("/additem")
    public ResponseEntity<AddItemCallResponse> addItem(@RequestBody AddItemCallRequest request) {
        AddItemCallResponse out = new AddItemCallResponse();

        Integer accountUID = extractAccountUID(request);
        ResponseEntity<AddItemCallResponse> authValidation = validateAuthenticatedCall(request == null ? null : request.getSessionToken(), accountUID, out);
        if (authValidation != null) {
            return authValidation;
        }

        if (request.getItem() == null || isBlank(request.getItem().getName()) || request.getItem().getStartingPrice() < 0) {
            return badRequest(error(out, 1, "Add-item requires a valid item with name and non-negative startingPrice."));
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return unauthorized(error(out, authErrorCode(authResp), "Authentication failed."));
        }

        try {
            request.getItem().setOwnerUid(accountUID);

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
                return downstreamError(error(out, 2, "Auction service unavailable."));
            }

            out.setErrorCode(resp.getErrorCode());
            out.setMessage(defaultMessage(resp.getErrorCode(), "Add item failed."));
            if (resp.getItems() != null && !resp.getItems().isEmpty()) {
                out.setItem(resp.getItems().get(0));
            } else {
                out.setItem(request.getItem());
            }

            return resp.getErrorCode() == 0 ? created(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Add item failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Auction service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Auction service request failed."));
        }
    }

    @DeleteMapping("/removeitem")
    public ResponseEntity<RemoveItemCallResponse> removeItem(@RequestBody RemoveItemCallRequest request) {
        RemoveItemCallResponse out = new RemoveItemCallResponse();

        Integer accountUID = extractAccountUID(request);
        ResponseEntity<RemoveItemCallResponse> authValidation = validateAuthenticatedCall(request == null ? null : request.getSessionToken(), accountUID, out);
        if (authValidation != null) {
            return authValidation;
        }

        if (request.getItemId() <= 0) {
            return badRequest(error(out, 1, "Remove-item requires a valid itemId."));
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return unauthorized(error(out, authErrorCode(authResp), "Authentication failed."));
        }

        try {
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
                return downstreamError(error(out, 2, "Auction service unavailable."));
            }

            out.setErrorCode(resp.getErrorCode());
            out.setMessage(defaultMessage(resp.getErrorCode(), "Remove item failed."));
            return resp.getErrorCode() == 0 ? ok(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Remove item failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Auction service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Auction service request failed."));
        }
    }

    @PutMapping("/modifyitem")
    public ResponseEntity<ModifyItemCallResponse> modifyItem(@RequestBody ModifyItemCallRequest request) {
        ModifyItemCallResponse out = new ModifyItemCallResponse();

        Integer accountUID = extractAccountUID(request);
        ResponseEntity<ModifyItemCallResponse> authValidation = validateAuthenticatedCall(request == null ? null : request.getSessionToken(), accountUID, out);
        if (authValidation != null) {
            return authValidation;
        }

        if (request.getItem() == null || request.getItem().getId() <= 0 || isBlank(request.getItem().getName())) {
            return badRequest(error(out, 1, "Modify-item requires a valid item with id and name."));
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return unauthorized(error(out, authErrorCode(authResp), "Authentication failed."));
        }

        try {
            if (request.getItem().getOwnerUid() == 0) {
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
                return downstreamError(error(out, 2, "Auction service unavailable."));
            }

            out.setErrorCode(resp.getErrorCode());
            out.setMessage(defaultMessage(resp.getErrorCode(), "Modify item failed."));
            if (resp.getItems() != null && !resp.getItems().isEmpty()) {
                out.setItem(resp.getItems().get(0));
            } else {
                out.setItem(request.getItem());
            }

            return resp.getErrorCode() == 0 ? ok(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Modify item failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Auction service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Auction service request failed."));
        }
    }

    @PostMapping("/ship")
    public ResponseEntity<ShipCallResponse> ship(@RequestBody ShipCallRequest request) {
        ShipCallResponse out = new ShipCallResponse();

        Integer accountUID = extractAccountUID(request);
        ResponseEntity<ShipCallResponse> authValidation = validateAuthenticatedCall(request == null ? null : request.getSessionToken(), accountUID, out);
        if (authValidation != null) {
            return authValidation;
        }

        if (request.getTargetItem() == null || request.getTargetItem().getId() <= 0 || isBlank(request.getShippingAddress())) {
            return badRequest(error(out, 1, "Ship requires a targetItem with id and shippingAddress."));
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return unauthorized(error(out, authErrorCode(authResp), "Authentication failed."));
        }

        try {
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
                return downstreamError(error(out, 2, "Auction service unavailable."));
            }

            out.setErrorCode(resp.getErrorCode());
            out.setMessage(defaultMessage(resp.getErrorCode(), "Ship item failed."));
            return resp.getErrorCode() == 0 ? ok(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Ship item failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Auction service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Auction service request failed."));
        }
    }

    @PostMapping("/fetchaccount")
    public ResponseEntity<FetchAccountCallResponse> fetchAccount(@RequestBody FetchAccountCallRequest request) {
        FetchAccountCallResponse out = new FetchAccountCallResponse();

        Integer accountUID = extractAccountUID(request);
        ResponseEntity<FetchAccountCallResponse> authValidation = validateAuthenticatedCall(request == null ? null : request.getSessionToken(), accountUID, out);
        if (authValidation != null) {
            return authValidation;
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return unauthorized(error(out, authErrorCode(authResp), "Authentication failed."));
        }

        try {
            AccountServiceAuthenticatedRequest forward = new AccountServiceAuthenticatedRequest();
            forward.setSecret(authResp.getAuthenticatedRequest().getSecret());
            forward.setRequest(buildAccountLookupRequest(accountUID));

            AccountServiceAccountDatabaseResponse resp = restTemplate.postForObject(
                accountBaseUrl + "/fetchaccount",
                forward,
                AccountServiceAccountDatabaseResponse.class
            );

            if (resp == null) {
                return downstreamError(error(out, 2, "Account service unavailable."));
            }

            out.setErrorCode(resp.getErrorCode());
            out.setMessage(defaultMessage(resp.getErrorCode(), "Fetch account failed."));
            if (resp.getAccounts() != null && !resp.getAccounts().isEmpty()) {
                out.setAccount(resp.getAccounts().get(0));
            }

            return resp.getErrorCode() == 0 ? ok(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Fetch account failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Account service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Account service request failed."));
        }
    }

    @PostMapping("/fetchitems")
    public ResponseEntity<FetchItemsCallResponse> fetchItems(@RequestBody FetchItemsCallRequest request) {
        FetchItemsCallResponse out = new FetchItemsCallResponse();

        Integer accountUID = extractAccountUID(request);
        ResponseEntity<FetchItemsCallResponse> authValidation = validateAuthenticatedCall(request == null ? null : request.getSessionToken(), accountUID, out);
        if (authValidation != null) {
            return authValidation;
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return unauthorized(error(out, authErrorCode(authResp), "Authentication failed."));
        }

        try {
            ResponseEntity<AuctionServiceAuctionDatabaseResponse> entity = restTemplate.exchange(
                auctionBaseUrl + "/api/auction/catalogue",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                AuctionServiceAuctionDatabaseResponse.class
            );

            AuctionServiceAuctionDatabaseResponse resp = entity.getBody();
            if (resp == null) {
                return downstreamError(error(out, 2, "Auction service unavailable."));
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

            return resp.getErrorCode() == 0 ? ok(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Fetch items failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Auction service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Auction service request failed."));
        }
    }

    @PostMapping("/fetchreceipts")
    public ResponseEntity<FetchReceiptsCallResponse> fetchReceipts(@RequestBody FetchReceiptsCallRequest request) {
        FetchReceiptsCallResponse out = new FetchReceiptsCallResponse();

        Integer accountUID = extractAccountUID(request);
        ResponseEntity<FetchReceiptsCallResponse> authValidation = validateAuthenticatedCall(request == null ? null : request.getSessionToken(), accountUID, out);
        if (authValidation != null) {
            return authValidation;
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return unauthorized(error(out, authErrorCode(authResp), "Authentication failed."));
        }

        try {
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
                return downstreamError(error(out, 2, "Payment service unavailable."));
            }

            out.setErrorCode(resp.getErrorCode());
            out.setMessage(defaultMessage(resp.getErrorCode(), "Fetch receipts failed."));
            out.setReceipts(resp.getReceipts());

            return resp.getErrorCode() == 0 ? ok(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Fetch receipts failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Payment service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Payment service request failed."));
        }
    }

    @PostMapping("/fetchpendingpayments")
    public ResponseEntity<FetchPendingPaymentsCallResponse> fetchPendingPayments(@RequestBody FetchPendingPaymentsCallRequest request) {
        FetchPendingPaymentsCallResponse out = new FetchPendingPaymentsCallResponse();

        Integer accountUID = extractAccountUID(request);
        ResponseEntity<FetchPendingPaymentsCallResponse> authValidation = validateAuthenticatedCall(request == null ? null : request.getSessionToken(), accountUID, out);
        if (authValidation != null) {
            return authValidation;
        }

        AccountServiceAuthenticationResponse authResp = authenticateWithAccountService(request.getSessionToken(), accountUID);
        if (!isAuthenticated(authResp)) {
            return unauthorized(error(out, authErrorCode(authResp), "Authentication failed."));
        }

        try {
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
                return downstreamError(error(out, 2, "Payment service unavailable."));
            }

            out.setErrorCode(resp.getErrorCode());
            out.setMessage(defaultMessage(resp.getErrorCode(), "Fetch pending payments failed."));
            out.setPendingPayments(resp.getPendingPayments());

            return resp.getErrorCode() == 0 ? ok(out) : conflict(out);
        } catch (HttpStatusCodeException ex) {
            return mapDownstreamStatus(ex.getStatusCode(), error(out, 2, "Fetch pending payments failed."));
        } catch (ResourceAccessException ex) {
            return downstreamError(error(out, 2, "Payment service unavailable."));
        } catch (RestClientException ex) {
            return downstreamError(error(out, 2, "Payment service request failed."));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Response> health() {
        Response out = new Response();
        out.setErrorCode(0);
        out.setMessage("Gateway is healthy.");
        return ok(out);
    }

    private AccountServiceAuthenticationResponse authenticateWithAccountService(String sessionToken, Integer accountUID) {
        if (isBlank(sessionToken) || accountUID == null || accountUID <= 0) {
            return null;
        }

        try {
            Map<String, Object> authRequest = new HashMap<>();
            authRequest.put("sessionToken", sessionToken);
            authRequest.put("request", buildAccountLookupRequest(accountUID));

            return restTemplate.postForObject(
                accountBaseUrl + "/authenticate",
                authRequest,
                AccountServiceAuthenticationResponse.class
            );
        } catch (HttpStatusCodeException ex) {
            return null;
        } catch (RestClientException ex) {
            return null;
        }
    }

    private AccountServiceFetchAccountRequest buildAccountLookupRequest(int accountUID) {
        AccountServiceFetchAccountRequest request = new AccountServiceFetchAccountRequest();
        request.setAccountUID(accountUID);
        return request;
    }

    private boolean isAuthenticated(AccountServiceAuthenticationResponse authResp) {
        return authResp != null
            && authResp.getErrorCode() == 0
            && authResp.getAuthenticatedRequest() != null
            && !isBlank(authResp.getAuthenticatedRequest().getSecret());
    }

    private int authErrorCode(AccountServiceAuthenticationResponse authResp) {
        return authResp == null ? 2 : authResp.getErrorCode();
    }

    private Integer extractAccountUID(Object request) {
        if (request == null) {
            return null;
        }

        String[] methodNames = { "getAccountUID", "getAccountUid" };
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
                if (value instanceof String && !((String) value).trim().isEmpty()) {
                    return Integer.parseInt(((String) value).trim());
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

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String defaultMessage(int errorCode, String fallback) {
        return errorCode == 0 ? "Success." : fallback;
    }

    private <T extends Response> ResponseEntity<T> validateAuthenticatedCall(String sessionToken, Integer accountUID, T out) {
        if (accountUID == null || accountUID <= 0) {
            return badRequest(error(out, 1, "Authenticated requests require a valid accountUID."));
        }
        if (isBlank(sessionToken)) {
            return unauthorized(error(out, 2, "Authentication failed."));
        }
        return null;
    }

    private <T extends Response> T error(T response, int errorCode, String message) {
        response.setErrorCode(errorCode);
        response.setMessage(message);
        return response;
    }

    private <T extends Response> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    private <T extends Response> ResponseEntity<T> created(T body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    private <T extends Response> ResponseEntity<T> badRequest(T body) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    private <T extends Response> ResponseEntity<T> unauthorized(T body) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    private <T extends Response> ResponseEntity<T> conflict(T body) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    private <T extends Response> ResponseEntity<T> downstreamError(T body) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
    }

    private <T extends Response> ResponseEntity<T> mapDownstreamStatus(org.springframework.http.HttpStatusCode status, T body) {
        int code = status.value();

        if (code == 401 || code == 403) {
            return unauthorized(body);
        }
        if (code == 400) {
            return badRequest(body);
        }
        if (code == 404 || code == 409) {
            return conflict(body);
        }
        return downstreamError(body);
    }
}

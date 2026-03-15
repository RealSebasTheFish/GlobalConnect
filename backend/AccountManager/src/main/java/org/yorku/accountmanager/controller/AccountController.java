package org.yorku.accountmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.yorku.accountmanager.dto.*;
import org.yorku.accountmanager.service.AccountService;

@RestController
@RequestMapping("/")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AccountDatabaseResponse> signup(@RequestBody RegisterRequest req) {
        AccountDatabaseResponse resp = accountService.createAccount(req);

        HttpStatus status =
                (resp.getErrorCode() == 0) ? HttpStatus.OK :
                (resp.getErrorCode() == 1) ? HttpStatus.CONFLICT :
                HttpStatus.INTERNAL_SERVER_ERROR;

    return new ResponseEntity<>(resp, status);
    }

    @PostMapping("/login")
    public ResponseEntity<SessionResponse> login(@RequestBody LoginRequest req) {
        SessionResponse resp = accountService.createSession(req);

        HttpStatus status =
                (resp.getErrorCode() == 0) ? HttpStatus.OK :
                (resp.getErrorCode() == 5) ? HttpStatus.UNAUTHORIZED :
                HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(resp, status);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthRequest req) {
        AuthenticationResponse resp = accountService.authenticateRequest(req);

        HttpStatus status =
                (resp.getErrorCode() == 0) ? HttpStatus.OK :
                (resp.getErrorCode() == 4) ? HttpStatus.UNAUTHORIZED :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(resp, status);
    }

    @PostMapping("/forgotpassword/request")
    public ResponseEntity<ForgotPasswordStartResponse> forgotPasswordRequest(@RequestBody ForgotPasswordStartRequest req) {
        ForgotPasswordStartResponse resp = accountService.createRescueCode(req);

        HttpStatus status =
                (resp.getErrorCode() == 0) ? HttpStatus.OK :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(resp, status);
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<AccountDatabaseResponse> forgotPassword(@RequestBody ForgotPasswordRequest req) {
        AccountDatabaseResponse resp = accountService.resetPassword(req);

        HttpStatus status =
                (resp.getErrorCode() == 0) ? HttpStatus.OK :
                (resp.getErrorCode() == 3) ? HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(resp, status);
    }

    @PostMapping("/fetchaccount")
    public ResponseEntity<AccountDatabaseResponse> fetchAccount(@RequestBody AuthenticatedRequest req) {
        AccountDatabaseResponse resp = accountService.fetchAccount(req);

        HttpStatus status =
                (resp.getErrorCode() == 0) ? HttpStatus.OK :
                (resp.getErrorCode() == 4) ? HttpStatus.UNAUTHORIZED :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(resp, status);
    }
}
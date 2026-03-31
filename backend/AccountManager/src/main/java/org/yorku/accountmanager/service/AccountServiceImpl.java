package org.yorku.accountmanager.service;

import org.springframework.stereotype.Service;

import org.yorku.accountmanager.dao.AccountDatabaseManager;
import org.yorku.accountmanager.dto.*;
import org.yorku.accountmanager.model.Account;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDatabaseManager accountDb;
    private final SessionManager sessionManager;

    public AccountServiceImpl(AccountDatabaseManager accountDb, SessionManager sessionManager) {
        this.accountDb = accountDb;
        this.sessionManager = sessionManager;
    }

    @Override
    public AccountDatabaseResponse createAccount(RegisterRequest req) {
        Account a = new Account();
        a.setUsername(req.getUsername());
        a.setEmail(req.getEmail());
        a.setHashedPassword(PasswordUtil.sha256(req.getPassword()));

        a.setFirstName(req.getFirstName());
        a.setLastName(req.getLastName());
        a.setStreetName(req.getStreetName());
        a.setStreetNumber(req.getStreetNumber());
        a.setCity(req.getCity());
        a.setCountry(req.getCountry());
        a.setPostalCode(req.getPostalCode());

        return accountDb.createAccount(a);
    }

    @Override
    public SessionResponse createSession(LoginRequest req) {
        if (req == null || req.getUsername() == null || req.getUsername().isBlank() || req.getPassword() == null) {
            return new SessionResponse(5, null);
        }

        Integer accountUID = accountDb.findAccountUIDByUsername(req.getUsername());
        if (accountUID == null || accountUID <= 0) {
            return new SessionResponse(5, null);
        }

        String hashed = PasswordUtil.sha256(req.getPassword());
        SessionResponse response = sessionManager.createSession(accountUID, hashed);
        if (response.getErrorCode() == 0) {
            response.setAccountUID(accountUID);
        }
        return response;
    }

    @Override
    public AuthenticationResponse authenticateRequest(AuthRequest req) {
        return sessionManager.authenticateRequest(req.getSessionToken(), req.getRequest());
    }

    @Override
    public AccountDatabaseResponse resetPassword(ForgotPasswordRequest req) {
        //  validate rescue code (0 ok, 3 invalid, 2 server)
        AccountDatabaseResponse validation =
                accountDb.resetPassword(req.getAccountUID(), req.getForgotPasswordRescueCode());

        if (validation.getErrorCode() != 0) {
            return validation;
        }

        //  update the password
        String newHash = PasswordUtil.sha256(req.getNewPassword());
        boolean ok = accountDb.updatePassword(req.getAccountUID(), newHash);
        if (!ok) {
            return new AccountDatabaseResponse(2, java.util.List.of());
        }

        // invalidate code after use
        accountDb.deleteRescueCode(req.getAccountUID());

        return new AccountDatabaseResponse(0, java.util.List.of());
    }

    @Override
    public AccountDatabaseResponse fetchAccount(AuthenticatedRequest req) {
        return accountDb.fetchAccount(req);
    }

    @Override
    public ForgotPasswordStartResponse createRescueCode(ForgotPasswordStartRequest req) {
        String code = accountDb.createRescueCode(req.getAccountUID());
        if (code == null) {
            return new ForgotPasswordStartResponse(2, null);
        }
        return new ForgotPasswordStartResponse(0, code);
    }
}
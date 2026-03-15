package org.yorku.accountmanager.service;

import org.yorku.accountmanager.dto.*;

public interface AccountService {
    AccountDatabaseResponse createAccount(RegisterRequest req);
    SessionResponse createSession(LoginRequest req);
    AuthenticationResponse authenticateRequest(AuthRequest req);
    AccountDatabaseResponse resetPassword(ForgotPasswordRequest req);
    AccountDatabaseResponse fetchAccount(AuthenticatedRequest req);

    // helper to support forgotpassword  demo
    ForgotPasswordStartResponse createRescueCode(ForgotPasswordStartRequest req);
}
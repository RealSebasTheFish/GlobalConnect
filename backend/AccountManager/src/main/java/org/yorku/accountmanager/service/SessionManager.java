package org.yorku.accountmanager.service;

import org.yorku.accountmanager.dto.AuthenticationResponse;
import org.yorku.accountmanager.dto.SessionResponse;
import org.yorku.accountmanager.dto.Request;

public interface SessionManager {
    SessionResponse createSession(int accountUID, String hashedPassword);
    AuthenticationResponse authenticateRequest(String sessionToken, Request request);
}
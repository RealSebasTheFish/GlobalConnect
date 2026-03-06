package org.yorku.accountmanager.service;

import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.yorku.accountmanager.dao.AccountDatabaseManager;
import org.yorku.accountmanager.dto.*;

@Service
public class SessionManagerImpl implements SessionManager {

    private final AccountDatabaseManager db;
    private final JdbcTemplate jdbc;

    public SessionManagerImpl(AccountDatabaseManager db, JdbcTemplate jdbc) {
        this.db = db;
        this.jdbc = jdbc;
    }

    // errorCode: 0 success, 5 invalid creds, 2 server error
    @Override
    public SessionResponse createSession(int accountUID, String hashedPassword) {
        try {
            boolean ok = db.checkCreds(accountUID, hashedPassword);
            if (!ok) return new SessionResponse(5, null);

            String token = UUID.randomUUID().toString();
            long now = System.currentTimeMillis();

            jdbc.update(
                    "INSERT INTO sessions (sessionToken, accountUID, createdAt) VALUES (?,?,?)",
                    token, accountUID, now
            );

            return new SessionResponse(0, token);
        } catch (Exception e) {
            return new SessionResponse(2, null);
        }
    }

    // errorCode: 0 success, 4 invalid session, 2 server error
    @Override
    public AuthenticationResponse authenticateRequest(String sessionToken, Request request) {
        try {
            // query returns empty list if not found (no exception)
            var uids = jdbc.query(
                    "SELECT accountUID FROM sessions WHERE sessionToken = ?",
                    (rs, rowNum) -> rs.getInt("accountUID"),
                    sessionToken
            );

            if (uids.isEmpty()) {
                return new AuthenticationResponse(4, null); // invalid session
            }

            String secret = PasswordUtil.sha256(sessionToken + ":" + System.currentTimeMillis());

            AuthenticatedRequest ar = new AuthenticatedRequest();
            ar.setSecret(secret);
            ar.setRequest(request);

            return new AuthenticationResponse(0, ar);

        } catch (Exception e) {
            return new AuthenticationResponse(2, null);
        }
    }
}
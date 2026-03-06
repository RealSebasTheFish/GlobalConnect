package org.yorku.accountmanager.dao;

import org.yorku.accountmanager.dto.AccountDatabaseResponse;
import org.yorku.accountmanager.dto.AuthenticatedRequest;
import org.yorku.accountmanager.model.Account;

public interface AccountDatabaseManager {

    AccountDatabaseResponse createAccount(Account account);
    AccountDatabaseResponse resetPassword(int accountUID, String forgotPasswordRescueCode);
    AccountDatabaseResponse fetchAccount(AuthenticatedRequest authenticatedRequest);
    boolean checkCreds(int accountUID, String hashedPassword);
    //Stub
    AccountDatabaseResponse pushAuctionUpdate(Object auctionUpdate);

    String createRescueCode(int accountUID);
    boolean updatePassword(int accountUID, String newHashedPassword);
    boolean deleteRescueCode(int accountUID);
}
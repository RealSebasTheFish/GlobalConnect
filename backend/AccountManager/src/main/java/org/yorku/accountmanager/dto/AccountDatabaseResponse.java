package org.yorku.accountmanager.dto;

import java.util.List;
import org.yorku.accountmanager.model.Account;

public class AccountDatabaseResponse {
    private int errorCode;
    private List<Account> accounts;

    public AccountDatabaseResponse() {}

    public AccountDatabaseResponse(int errorCode, List<Account> accounts) {
        this.errorCode = errorCode;
        this.accounts = accounts;
    }

    public int getErrorCode() { return errorCode; }
    public void setErrorCode(int errorCode) { this.errorCode = errorCode; }

    public List<Account> getAccounts() { return accounts; }
    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }
}
package org.yorku.gatewaymanager.dto.micro.account;

import java.util.ArrayList;
import java.util.List;
import org.yorku.gatewaymanager.dto.common.Account;

public class AccountServiceAccountDatabaseResponse {
    private int errorCode;
    private List<Account> accounts = new ArrayList<>();

    public AccountServiceAccountDatabaseResponse() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}

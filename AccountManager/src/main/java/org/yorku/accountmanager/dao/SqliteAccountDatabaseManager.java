package org.yorku.accountmanager.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import org.yorku.accountmanager.dto.AccountDatabaseResponse;
import org.yorku.accountmanager.dto.AuthenticatedRequest;
import org.yorku.accountmanager.model.Account;

@Repository
public class SqliteAccountDatabaseManager implements AccountDatabaseManager {

    private final JdbcTemplate jdbc;

    public SqliteAccountDatabaseManager(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // errorCode 0 success
    // errorCode 1 username exists
    // errorCode 2 internal server error
    @Override
    public AccountDatabaseResponse createAccount(Account account) {
        try {
            Integer count = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM accounts WHERE username = ?",
                    Integer.class,
                    account.getUsername()
            );

            if (count != null && count > 0) {
                return new AccountDatabaseResponse(1, List.of());
            }

            KeyHolder kh = new GeneratedKeyHolder();

            jdbc.update(con -> {
                var ps = con.prepareStatement(
                        "INSERT INTO accounts (username, email, hashedPassword, firstName, lastName, streetName, streetNumber, city, country, postalCode) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        new String[] { "accountUID" }
                );

                ps.setString(1, account.getUsername());
                ps.setString(2, account.getEmail());
                ps.setString(3, account.getHashedPassword());
                ps.setString(4, account.getFirstName());
                ps.setString(5, account.getLastName());
                ps.setString(6, account.getStreetName());
                ps.setString(7, account.getStreetNumber());
                ps.setString(8, account.getCity());
                ps.setString(9, account.getCountry());
                ps.setString(10, account.getPostalCode());
                return ps;
            }, kh);

            Number id = kh.getKey();
            if (id == null) {
                return new AccountDatabaseResponse(2, List.of());
            }

            account.setAccountUID(id.intValue());

            account.setHashedPassword(null);

            return new AccountDatabaseResponse(0, List.of(account));

        } catch (Exception e) {
            return new AccountDatabaseResponse(2, List.of());
        }
    }

    // errorCode 0 success
    // errorCode 2 internal server error
    // errorCode 3 invalid reset code
    @Override
    public AccountDatabaseResponse resetPassword(int accountUID, String forgotPasswordRescueCode) {
        try {
            Integer count = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM password_resets WHERE accountUID = ? AND rescueCode = ?",
                    Integer.class,
                    accountUID,
                    forgotPasswordRescueCode
            );

            if (count == null || count == 0) {
                return new AccountDatabaseResponse(3, List.of());
            }

            return new AccountDatabaseResponse(0, List.of());

        } catch (Exception e) {
            return new AccountDatabaseResponse(2, List.of());
        }
    }

    // errorCode 0 success
    // errorCode 2 internal server error
    @Override
    public AccountDatabaseResponse fetchAccount(AuthenticatedRequest authenticatedRequest) {
        try {
            if (authenticatedRequest == null || authenticatedRequest.getRequest() == null) {
                return new AccountDatabaseResponse(2, List.of());
            }

            int uid = authenticatedRequest.getRequest().getAccountUID();

            List<Account> accounts = jdbc.query(
                    "SELECT accountUID, username, email, hashedPassword, firstName, lastName, streetName, streetNumber, city, country, postalCode " +
                    "FROM accounts WHERE accountUID = ?",
                    (rs, rowNum) -> {
                        Account a = new Account();
                        a.setAccountUID(rs.getInt("accountUID"));
                        a.setUsername(rs.getString("username"));
                        a.setEmail(rs.getString("email"));
                        a.setHashedPassword(rs.getString("hashedPassword"));
                        a.setFirstName(rs.getString("firstName"));
                        a.setLastName(rs.getString("lastName"));
                        a.setStreetName(rs.getString("streetName"));
                        a.setStreetNumber(rs.getString("streetNumber"));
                        a.setCity(rs.getString("city"));
                        a.setCountry(rs.getString("country"));
                        a.setPostalCode(rs.getString("postalCode"));
                        return a;
                    },
                    uid
            );

            if (accounts.isEmpty()) {
                return new AccountDatabaseResponse(2, List.of());
            }

            Account a = accounts.get(0);
            a.setHashedPassword(null);

            return new AccountDatabaseResponse(0, List.of(a));

        } catch (Exception e) {
            return new AccountDatabaseResponse(2, List.of());
        }
    }

    @Override
    public boolean checkCreds(int accountUID, String hashedPassword) {
        try {
            Integer count = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM accounts WHERE accountUID = ? AND hashedPassword = ?",
                    Integer.class,
                    accountUID,
                    hashedPassword
            );
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
    //Stub
    @Override
    public AccountDatabaseResponse pushAuctionUpdate(Object auctionUpdate) {
        return new AccountDatabaseResponse(0, List.of());
    }

    //  a rescue code for demonstration
    @Override
    public String createRescueCode(int accountUID) {
        try {
            // ensure account exists
            Integer exists = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM accounts WHERE accountUID = ?",
                    Integer.class,
                    accountUID
            );
            if (exists == null || exists == 0) return null;

            String code = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            long now = System.currentTimeMillis();

            // one code per account: replace existing
            jdbc.update("DELETE FROM password_resets WHERE accountUID = ?", accountUID);

            jdbc.update(
                    "INSERT INTO password_resets (accountUID, rescueCode, createdAt) VALUES (?, ?, ?)",
                    accountUID, code, now
            );

            return code;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean updatePassword(int accountUID, String newHashedPassword) {
        try {
            int updated = jdbc.update(
                    "UPDATE accounts SET hashedPassword = ? WHERE accountUID = ?",
                    newHashedPassword, accountUID
            );
            return updated > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteRescueCode(int accountUID) {
        try {
            jdbc.update("DELETE FROM password_resets WHERE accountUID = ?", accountUID);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
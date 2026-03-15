package org.yorku.gatewaymanager.model;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private int accountUID;
    private String username;
    private String hashedPassword;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;
    private List<Integer> catalogueIds = new ArrayList<>();

    public Account() {
    }

    public int getAccountUID() {
        return accountUID;
    }

    public void setAccountUID(int accountUID) {
        this.accountUID = accountUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Integer> getCatalogueIds() {
        return catalogueIds;
    }

    public void setCatalogueIds(List<Integer> catalogueIds) {
        this.catalogueIds = catalogueIds;
    }
}

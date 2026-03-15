package org.yorku.gatewaymanager.dto.call.signup;


import org.yorku.gatewaymanager.dto.common.Address;
import org.yorku.gatewaymanager.dto.common.Request;


public class SignUpCallRequest extends Request {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Address address;

    public SignUpCallRequest() {
        super("SignUpCallRequest");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
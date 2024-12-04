package com.hospital.model;

public class User extends BaseEntity {
    private String password;
    private String email;
    private String phoneNumber;
    private String address;

    public User(int id, String name, String username, String role, int hospitalId) {
        super(id, name, username, role, hospitalId);
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
} 
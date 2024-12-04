package com.hospital.model;

public class Hospital {
    private int id;
    private String name;
    private String address;
    private String contactNumber;

    public Hospital(int id, String name, String address, String contactNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
} 
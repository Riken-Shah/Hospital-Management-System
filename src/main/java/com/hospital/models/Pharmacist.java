package com.hospital.models;

public class Pharmacist extends User {
    private String licenseNumber;
    private boolean available;
    private String contactNumber;

    public Pharmacist(int id, String name, String username, int hospitalId, String contactNumber) {
        super(id, name, username, "PHARMACIST", hospitalId);
        this.contactNumber = contactNumber;
        this.available = true;
        this.licenseNumber = "PH" + id; // Default license number format
    }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    @Override
    public String toString() {
        return getName();
    }
} 
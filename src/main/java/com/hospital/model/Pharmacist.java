package com.hospital.model;

public class Pharmacist extends BaseEntity {
    private String licenseNumber;
    private String contactNumber;

    public Pharmacist(int id, String name, String username, int hospitalId, String licenseNumber) {
        super(id, name, username, "PHARMACIST", hospitalId);
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getPharmacistId() {
        return getId();
    }
} 
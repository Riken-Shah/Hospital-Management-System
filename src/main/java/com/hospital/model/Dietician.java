package com.hospital.model;

public class Dietician extends BaseEntity {
    private String specialization;
    private String licenseNumber;
    private String contactNumber;

    public Dietician(int id, String name, String username, int hospitalId, String specialization, String licenseNumber) {
        super(id, name, username, "DIETICIAN", hospitalId);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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

    public int getDieticianId() {
        return getId();
    }
} 
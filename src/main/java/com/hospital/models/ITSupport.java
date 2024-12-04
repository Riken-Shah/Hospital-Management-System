package com.hospital.models;

public class ITSupport extends User {
    private String specialization;
    private String contactNumber;
    private boolean available;

    public ITSupport(int id, String name, String username, int hospitalId, String specialization, String contactNumber) {
        super(id, name, username, "IT_SUPPORT", hospitalId);
        this.specialization = specialization;
        this.contactNumber = contactNumber;
        this.available = true;
    }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return getName();
    }
} 
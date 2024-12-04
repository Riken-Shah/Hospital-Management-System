package com.hospital.models;

public class Pharmacist {
    private int id;
    private int userId;
    private int hospitalId;
    private String name;
    private String contactNumber;
    private boolean available;
    private String licenseNumber;
    
    public Pharmacist(int id, int userId, int hospitalId, String name, String contactNumber) {
        this.id = id;
        this.userId = userId;
        this.hospitalId = hospitalId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.available = true;
        this.licenseNumber = "PH" + id; // Default license number format
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    @Override
    public String toString() {
        return name;
    }
} 
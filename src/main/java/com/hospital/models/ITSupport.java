package com.hospital.models;

public class ITSupport {
    private int id;
    private int userId;
    private int hospitalId;
    private String name;
    private String specialization;
    private String contactNumber;
    private boolean available;

    public ITSupport(int id, int userId, int hospitalId, String name, String specialization, String contactNumber) {
        this.id = id;
        this.userId = userId;
        this.hospitalId = hospitalId;
        this.name = name;
        this.specialization = specialization;
        this.contactNumber = contactNumber;
        this.available = true;
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

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return name;
    }
} 
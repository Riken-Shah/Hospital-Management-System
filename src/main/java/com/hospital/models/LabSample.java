package com.hospital.models;

import java.time.LocalDateTime;

public class LabSample {
    private int id;
    private int patientId;
    private int technicianId;
    private String sampleType;
    private String status;
    private LocalDateTime collectionDate;
    private LocalDateTime expiryDate;
    private String storageLocation;
    private String notes;
    
    public LabSample(int id, int patientId, int technicianId, String sampleType) {
        this.id = id;
        this.patientId = patientId;
        this.technicianId = technicianId;
        this.sampleType = sampleType;
        this.status = "ACTIVE";
        this.collectionDate = LocalDateTime.now();
        this.expiryDate = collectionDate.plusDays(7); // Default expiry of 7 days
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    
    public int getTechnicianId() { return technicianId; }
    public void setTechnicianId(int technicianId) { this.technicianId = technicianId; }
    
    public String getSampleType() { return sampleType; }
    public void setSampleType(String sampleType) { this.sampleType = sampleType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getCollectionDate() { return collectionDate; }
    public void setCollectionDate(LocalDateTime collectionDate) { this.collectionDate = collectionDate; }
    
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    
    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
} 
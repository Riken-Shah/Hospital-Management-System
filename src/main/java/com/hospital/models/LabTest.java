package com.hospital.models;

import java.time.LocalDateTime;

public class LabTest {
    private int id;
    private int patientId;
    private int technicianId;
    private String testType;
    private String status;
    private LocalDateTime testDate;
    private String result;
    private String notes;
    
    public LabTest(int id, int patientId, int technicianId, String testType, String status) {
        this.id = id;
        this.patientId = patientId;
        this.technicianId = technicianId;
        this.testType = testType;
        this.status = status;
        this.testDate = LocalDateTime.now();
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    
    public int getTechnicianId() { return technicianId; }
    public void setTechnicianId(int technicianId) { this.technicianId = technicianId; }
    
    public String getTestType() { return testType; }
    public void setTestType(String testType) { this.testType = testType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getTestDate() { return testDate; }
    public void setTestDate(LocalDateTime testDate) { this.testDate = testDate; }
    
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
} 
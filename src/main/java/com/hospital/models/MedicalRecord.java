package com.hospital.models;

import java.time.LocalDateTime;

public class MedicalRecord {
    private int id;
    private int patientId;
    private int doctorId;
    private String type;
    private String description;
    private LocalDateTime date;
    private String attachments;

    public MedicalRecord(int id, int patientId, int doctorId, String type, String description) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.type = type;
        this.description = description;
        this.date = LocalDateTime.now();
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    
    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }
} 
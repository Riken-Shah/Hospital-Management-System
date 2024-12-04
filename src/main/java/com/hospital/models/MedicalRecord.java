package com.hospital.models;

import java.time.LocalDateTime;

public class MedicalRecord {
    private int id;
    private int patientId;
    private int doctorId;
    private String type;
    private String description;
    private LocalDateTime date;

    public MedicalRecord(int id, int patientId, int doctorId, String type, String description) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.type = type;
        this.description = description;
        this.date = LocalDateTime.now();
    }

    // Getters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public LocalDateTime getDate() { return date; }

    // Setters
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(LocalDateTime date) { this.date = date; }
} 
package com.hospital.models;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private LocalDateTime dateTime;
    private String purpose;
    private String status;
    private String notes;
    private String diagnosis;
    private int duration; // in minutes
    private String location;
    private String type;
    private String room;

    public Appointment(int id, int patientId, int doctorId, LocalDateTime dateTime, String purpose, String status, String notes, String diagnosis, int duration, String room) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.purpose = purpose;
        this.status = status;
        this.notes = notes;
        this.diagnosis = diagnosis;
        this.duration = duration;
        this.room = room;
        this.type = purpose; // Using purpose as type for backward compatibility
    }

    // Getters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getPurpose() { return purpose; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public String getDiagnosis() { return diagnosis; }
    public int getDuration() { return duration; }
    public String getLocation() { return location; }
    public String getType() { return type; }
    public String getRoom() { return room; }

    // Setters
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setLocation(String location) { this.location = location; }
    public void setType(String type) { this.type = type; }
    public void setRoom(String room) { this.room = room; }
} 
package com.hospital.models;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private LocalDateTime dateTime;
    private String type;
    private String status;
    private String notes;
    private String description;
    private int duration;
    private String room;

    public Appointment(int id, int patientId, int doctorId, LocalDateTime dateTime,
                      String type, String status, String notes, String description,
                      int duration, String room) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.type = type;
        this.status = status;
        this.notes = notes;
        this.description = description;
        this.duration = duration;
        this.room = room;
    }

    // Getters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public String getRoom() { return room; }

    // Setters
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public void setType(String type) { this.type = type; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setDescription(String description) { this.description = description; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setRoom(String room) { this.room = room; }
} 
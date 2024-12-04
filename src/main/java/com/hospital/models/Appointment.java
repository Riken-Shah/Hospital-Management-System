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
    private String reason;
    private int duration; // in minutes
    private String room;

    public Appointment(int id, int patientId, int doctorId, LocalDateTime dateTime, String type,
                      String status, String notes, String reason, int duration, String room) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.type = type;
        this.status = status;
        this.notes = notes;
        this.reason = reason;
        this.duration = duration;
        this.room = room;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
} 
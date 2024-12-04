package com.hospital.models;

import java.time.LocalDateTime;

public class DietConsultation {
    private int id;
    private int patientId;
    private int dieticianId;
    private LocalDateTime consultationDate;
    private String notes;
    private String recommendations;
    private String status;
    private LocalDateTime nextAppointment;

    public DietConsultation(int id, int patientId, int dieticianId, LocalDateTime consultationDate,
                           String notes, String recommendations, String status, LocalDateTime nextAppointment) {
        this.id = id;
        this.patientId = patientId;
        this.dieticianId = dieticianId;
        this.consultationDate = consultationDate;
        this.notes = notes;
        this.recommendations = recommendations;
        this.status = status;
        this.nextAppointment = nextAppointment;
    }

    // Getters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDieticianId() { return dieticianId; }
    public LocalDateTime getConsultationDate() { return consultationDate; }
    public String getNotes() { return notes; }
    public String getRecommendations() { return recommendations; }
    public String getStatus() { return status; }
    public LocalDateTime getNextAppointment() { return nextAppointment; }

    // Setters
    public void setNotes(String notes) { this.notes = notes; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }
    public void setStatus(String status) { this.status = status; }
    public void setNextAppointment(LocalDateTime nextAppointment) { this.nextAppointment = nextAppointment; }
} 
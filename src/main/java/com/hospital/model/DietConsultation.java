package com.hospital.model;

import java.time.LocalDateTime;

public class DietConsultation {
    private int id;
    private int patientId;
    private int dieticianId;
    private LocalDateTime consultationDate;
    private String notes;
    private String status;
    private LocalDateTime completedDate;

    public DietConsultation(int id, int patientId, int dieticianId, LocalDateTime consultationDate, String notes, String status) {
        this.id = id;
        this.patientId = patientId;
        this.dieticianId = dieticianId;
        this.consultationDate = consultationDate;
        this.notes = notes;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDieticianId() {
        return dieticianId;
    }

    public void setDieticianId(int dieticianId) {
        this.dieticianId = dieticianId;
    }

    public LocalDateTime getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(LocalDateTime consultationDate) {
        this.consultationDate = consultationDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }
} 
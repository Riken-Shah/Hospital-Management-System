package com.hospital.models;

import java.time.LocalDate;

public class DietConsultation {
    private int id;
    private int patientId;
    private int dieticianId;
    private LocalDate consultationDate;
    private String notes;
    private LocalDate nextAppointment;
    private String status;

    public DietConsultation(int id, int patientId, int dieticianId, LocalDate consultationDate, 
                          String notes, LocalDate nextAppointment) {
        this.id = id;
        this.patientId = patientId;
        this.dieticianId = dieticianId;
        this.consultationDate = consultationDate;
        this.notes = notes;
        this.nextAppointment = nextAppointment;
        this.status = "SCHEDULED";
    }

    // Getters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDieticianId() { return dieticianId; }
    public LocalDate getConsultationDate() { return consultationDate; }
    public String getNotes() { return notes; }
    public LocalDate getNextAppointment() { return nextAppointment; }
    public String getStatus() { return status; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setNextAppointment(LocalDate nextAppointment) { this.nextAppointment = nextAppointment; }
} 
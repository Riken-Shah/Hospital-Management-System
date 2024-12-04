package com.hospital.model;

import java.time.LocalDateTime;

public class Appointment extends BaseEntity {
    private int patientId;
    private int doctorId;
    private LocalDateTime appointmentDate;
    private String status;
    private String notes;

    public Appointment(int id, String name, String username, int hospitalId, int patientId, int doctorId, LocalDateTime appointmentDate) {
        super(id, name, username, "APPOINTMENT", hospitalId);
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.status = "SCHEDULED";
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
} 
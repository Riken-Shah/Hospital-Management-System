package com.hospital.model;

import java.time.LocalDateTime;

public class MedicalRecord extends BaseEntity {
    private int patientId;
    private int doctorId;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String notes;
    private LocalDateTime recordDate;

    public MedicalRecord(int id, String name, String username, int hospitalId, int patientId, int doctorId) {
        super(id, name, username, "MEDICAL_RECORD", hospitalId);
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.recordDate = LocalDateTime.now();
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

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDateTime recordDate) {
        this.recordDate = recordDate;
    }
} 
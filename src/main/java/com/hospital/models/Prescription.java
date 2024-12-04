package com.hospital.models;

import java.time.LocalDate;
import java.util.List;

public class Prescription {
    private int id;
    private int patientId;
    private int doctorId;
    private LocalDate date;
    private List<Medication> medications;
    private String diagnosis;
    private String instructions;
    private String notes;
    private LocalDate validUntil;
    private boolean isActive;
    private int refillsRemaining;

    public static class Medication {
        private String name;
        private String dosage;
        private String frequency;
        private int duration; // in days
        private String instructions;

        public Medication(String name, String dosage, String frequency, int duration, String instructions) {
            this.name = name;
            this.dosage = dosage;
            this.frequency = frequency;
            this.duration = duration;
            this.instructions = instructions;
        }

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDosage() { return dosage; }
        public void setDosage(String dosage) { this.dosage = dosage; }

        public String getFrequency() { return frequency; }
        public void setFrequency(String frequency) { this.frequency = frequency; }

        public int getDuration() { return duration; }
        public void setDuration(int duration) { this.duration = duration; }

        public String getInstructions() { return instructions; }
        public void setInstructions(String instructions) { this.instructions = instructions; }
    }

    public Prescription(int id, int patientId, int doctorId, LocalDate date, List<Medication> medications,
                       String diagnosis, String instructions, String notes, LocalDate validUntil,
                       boolean isActive, int refillsRemaining) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.medications = medications;
        this.diagnosis = diagnosis;
        this.instructions = instructions;
        this.notes = notes;
        this.validUntil = validUntil;
        this.isActive = isActive;
        this.refillsRemaining = refillsRemaining;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<Medication> getMedications() { return medications; }
    public void setMedications(List<Medication> medications) { this.medications = medications; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDate getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public int getRefillsRemaining() { return refillsRemaining; }
    public void setRefillsRemaining(int refillsRemaining) { this.refillsRemaining = refillsRemaining; }
} 
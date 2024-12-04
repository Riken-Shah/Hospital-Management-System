package com.hospital.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Prescription {
    private int id;
    private int patientId;
    private int doctorId;
    private LocalDateTime date;
    private String diagnosis;
    private String instructions;
    private String notes;
    private List<Medication> medications;
    private boolean active;

    public Prescription(int id, int patientId, int doctorId, String diagnosis, String instructions, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = LocalDateTime.now();
        this.diagnosis = diagnosis;
        this.instructions = instructions;
        this.notes = notes;
        this.medications = new ArrayList<>();
        this.active = true;
    }

    public static class Medication {
        private String name;
        private String dosage;
        private int duration;
        private String instructions;

        public Medication(String name, String dosage, int duration, String instructions) {
            this.name = name;
            this.dosage = dosage;
            this.duration = duration;
            this.instructions = instructions;
        }

        // Getters
        public String getName() { return name; }
        public String getDosage() { return dosage; }
        public int getDuration() { return duration; }
        public String getInstructions() { return instructions; }

        // Setters
        public void setName(String name) { this.name = name; }
        public void setDosage(String dosage) { this.dosage = dosage; }
        public void setDuration(int duration) { this.duration = duration; }
        public void setInstructions(String instructions) { this.instructions = instructions; }
    }

    // Getters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public LocalDateTime getDate() { return date; }
    public String getDiagnosis() { return diagnosis; }
    public String getInstructions() { return instructions; }
    public String getNotes() { return notes; }
    public List<Medication> getMedications() { return new ArrayList<>(medications); }
    public boolean isActive() { return active; }

    // Setters
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setMedications(List<Medication> medications) { this.medications = new ArrayList<>(medications); }
    public void setActive(boolean active) { this.active = active; }

    // Helper methods
    public void addMedication(Medication medication) {
        medications.add(medication);
    }

    public void removeMedication(Medication medication) {
        medications.remove(medication);
    }
} 
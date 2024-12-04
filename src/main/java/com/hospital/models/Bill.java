package com.hospital.models;

import java.time.LocalDateTime;

public class Bill {
    private int id;
    private int patientId;
    private String type;
    private double amount;
    private String notes;
    private LocalDateTime billDate;
    private String status;

    public Bill(int id, int patientId, String type, double amount, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.type = type;
        this.amount = amount;
        this.notes = notes;
        this.billDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    public Bill(int patientId, String type, double amount, String notes) {
        this.patientId = patientId;
        this.type = type;
        this.amount = amount;
        this.notes = notes;
        this.billDate = LocalDateTime.now();
        this.status = "PENDING";
        // ID will be set by DataStore
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getBillDate() { return billDate; }
    public void setBillDate(LocalDateTime billDate) { this.billDate = billDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 
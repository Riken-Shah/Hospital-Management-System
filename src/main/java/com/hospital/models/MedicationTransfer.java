package com.hospital.models;

import java.time.LocalDateTime;

public class MedicationTransfer {
    private int id;
    private int fromHospitalId;
    private int toHospitalId;
    private int medicationId;
    private int quantity;
    private String medicationName;
    private String status;
    private LocalDateTime transferDate;
    private String notes;
    
    public MedicationTransfer(int id, int fromHospitalId, int toHospitalId, int medicationId, int quantity) {
        this.id = id;
        this.fromHospitalId = fromHospitalId;
        this.toHospitalId = toHospitalId;
        this.medicationId = medicationId;
        this.quantity = quantity;
        this.status = "PENDING";
        this.transferDate = LocalDateTime.now();
    }
    
    public MedicationTransfer(int id, int fromHospitalId, int toHospitalId, String medicationName, int quantity, String status) {
        this.id = id;
        this.fromHospitalId = fromHospitalId;
        this.toHospitalId = toHospitalId;
        this.medicationName = medicationName;
        this.quantity = quantity;
        this.status = status;
        this.transferDate = LocalDateTime.now();
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getFromHospitalId() { return fromHospitalId; }
    public void setFromHospitalId(int fromHospitalId) { this.fromHospitalId = fromHospitalId; }
    
    public int getToHospitalId() { return toHospitalId; }
    public void setToHospitalId(int toHospitalId) { this.toHospitalId = toHospitalId; }
    
    public int getMedicationId() { return medicationId; }
    public void setMedicationId(int medicationId) { this.medicationId = medicationId; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getTransferDate() { return transferDate; }
    public void setTransferDate(LocalDateTime transferDate) { this.transferDate = transferDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
} 
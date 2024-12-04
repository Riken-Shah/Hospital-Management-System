package com.hospital.model;

import java.time.LocalDateTime;

public class MedicationTransfer {
    private int id;
    private String medicationName;
    private int sourceHospitalId;
    private int destinationHospitalId;
    private int quantity;
    private String notes;
    private String status;
    private LocalDateTime transferDate;

    public MedicationTransfer(int id, String medicationName, int sourceHospitalId, int destinationHospitalId, int quantity, String notes, LocalDateTime transferDate) {
        this.id = id;
        this.medicationName = medicationName;
        this.sourceHospitalId = sourceHospitalId;
        this.destinationHospitalId = destinationHospitalId;
        this.quantity = quantity;
        this.notes = notes;
        this.transferDate = transferDate;
        this.status = "PENDING";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public int getSourceHospitalId() {
        return sourceHospitalId;
    }

    public void setSourceHospitalId(int sourceHospitalId) {
        this.sourceHospitalId = sourceHospitalId;
    }

    public int getDestinationHospitalId() {
        return destinationHospitalId;
    }

    public void setDestinationHospitalId(int destinationHospitalId) {
        this.destinationHospitalId = destinationHospitalId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public LocalDateTime getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDateTime transferDate) {
        this.transferDate = transferDate;
    }
} 
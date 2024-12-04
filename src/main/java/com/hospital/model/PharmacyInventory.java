package com.hospital.model;

public class PharmacyInventory {
    private int id;
    private int hospitalId;
    private String medicationName;
    private int quantity;
    private String unit;
    private double unitPrice;
    private String expiryDate;

    public PharmacyInventory(int id, int hospitalId, String medicationName, int quantity, 
                            String unit, double unitPrice, String expiryDate) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.medicationName = medicationName;
        this.quantity = quantity;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
} 
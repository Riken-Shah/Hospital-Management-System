package com.hospital.models;

import java.time.LocalDate;
import java.math.BigDecimal;

public class PharmacyInventory {
    private int id;
    private String name;
    private String category;
    private int quantity;
    private String unit;
    private BigDecimal unitPrice;
    private LocalDate expiryDate;
    private String status;
    private String location;
    private int minimumStock;
    private int hospitalId;
    
    public PharmacyInventory(int id, String name, String category, int quantity, String unit, BigDecimal unitPrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.status = "AVAILABLE";
        this.minimumStock = 100;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        updateStatus();
    }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public int getMinimumStock() { return minimumStock; }
    public void setMinimumStock(int minimumStock) { 
        this.minimumStock = minimumStock;
        updateStatus();
    }
    
    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }
    
    private void updateStatus() {
        if (quantity <= 0) {
            status = "OUT_OF_STOCK";
        } else if (quantity < minimumStock) {
            status = "LOW_STOCK";
        } else {
            status = "AVAILABLE";
        }
    }
} 
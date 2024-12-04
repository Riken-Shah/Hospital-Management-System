package com.hospital.models;

public class LabInventory {
    private int id;
    private String name;
    private int quantity;
    private String unit;
    private int minimumStock;
    private String status;
    private String location;
    private String category;
    
    public LabInventory(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = "units";
        this.minimumStock = 10;
        this.status = "AVAILABLE";
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        updateStatus();
    }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public int getMinimumStock() { return minimumStock; }
    public void setMinimumStock(int minimumStock) { 
        this.minimumStock = minimumStock;
        updateStatus();
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
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
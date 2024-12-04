package com.hospital.models;

import java.time.LocalDate;

public class Medicine {
    private int id;
    private String name;
    private int quantity;
    private double unitPrice;
    private LocalDate expiryDate;
    private String form;  // e.g., Tablets, Capsules, Syrup
    private String category;  // e.g., Pain Relief, Antibiotics

    public Medicine(int id, String name, int quantity, double unitPrice, LocalDate expiryDate, 
                   String form, String category) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.expiryDate = expiryDate;
        this.form = form;
        this.category = category;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getForm() { return form; }
    public String getCategory() { return category; }

    // Setters
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public void setForm(String form) { this.form = form; }
    public void setCategory(String category) { this.category = category; }

    // Business logic
    public void addStock(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }

    public boolean removeStock(int amount) {
        if (amount > 0 && amount <= quantity) {
            this.quantity -= amount;
            return true;
        }
        return false;
    }

    public boolean isLowStock() {
        return quantity < 20; // Threshold for low stock
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }
} 
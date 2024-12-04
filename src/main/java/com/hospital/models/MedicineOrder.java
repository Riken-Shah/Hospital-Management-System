package com.hospital.models;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MedicineOrder {
    private int id;
    private int supplierId;
    private LocalDate orderDate;
    private String status;  // PENDING, DELIVERED, CANCELLED
    private Map<Integer, Integer> items;  // MedicineId -> Quantity

    public MedicineOrder(int id, int supplierId, LocalDate orderDate, String status) {
        this.id = id;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.status = status;
        this.items = new HashMap<>();
    }

    // Getters
    public int getId() { return id; }
    public int getSupplierId() { return supplierId; }
    public LocalDate getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
    public Map<Integer, Integer> getItems() { return new HashMap<>(items); }

    // Setters
    public void setStatus(String status) { this.status = status; }

    // Business logic
    public void addItem(int medicineId, int quantity) {
        items.put(medicineId, items.getOrDefault(medicineId, 0) + quantity);
    }

    public void removeItem(int medicineId) {
        items.remove(medicineId);
    }

    public void updateItemQuantity(int medicineId, int quantity) {
        if (quantity > 0) {
            items.put(medicineId, quantity);
        } else {
            items.remove(medicineId);
        }
    }

    public int getItemCount() {
        return items.size();
    }

    public int getTotalQuantity() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }
} 
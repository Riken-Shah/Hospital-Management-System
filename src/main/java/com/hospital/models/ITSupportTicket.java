package com.hospital.models;

import java.time.LocalDateTime;

public class ITSupportTicket {
    private int id;
    private int userId;
    private String category;
    private String description;
    private String priority;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime resolvedDate;
    private String resolution;
    private String assignedTo;
    private LocalDateTime resolvedAt;
    
    public ITSupportTicket(int id, int userId, String category, String description, String priority) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.status = "OPEN";
        this.createdDate = LocalDateTime.now();
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public LocalDateTime getResolvedDate() { return resolvedDate; }
    public void setResolvedDate(LocalDateTime resolvedDate) { this.resolvedDate = resolvedDate; }
    
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
    
    @Override
    public String toString() {
        return String.format("#%d - %s (%s)", id, category, status);
    }
} 
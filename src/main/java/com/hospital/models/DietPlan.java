package com.hospital.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DietPlan {
    private int id;
    private int patientId;
    private int dieticianId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String goal;
    private List<String> restrictions;
    private List<String> recommendations;
    private String notes;
    private String status;

    public DietPlan(int id, int patientId, int dieticianId, LocalDate startDate, LocalDate endDate,
                    String goal, List<String> restrictions, List<String> recommendations,
                    String notes, String status) {
        this.id = id;
        this.patientId = patientId;
        this.dieticianId = dieticianId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goal = goal;
        this.restrictions = new ArrayList<>(restrictions);
        this.recommendations = new ArrayList<>(recommendations);
        this.notes = notes;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDieticianId() { return dieticianId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getGoal() { return goal; }
    public List<String> getRestrictions() { return new ArrayList<>(restrictions); }
    public List<String> getRecommendations() { return new ArrayList<>(recommendations); }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }

    // Setters
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setGoal(String goal) { this.goal = goal; }
    public void setRestrictions(List<String> restrictions) { this.restrictions = new ArrayList<>(restrictions); }
    public void setRecommendations(List<String> recommendations) { this.recommendations = new ArrayList<>(recommendations); }
    public void setNotes(String notes) { this.notes = notes; }
    public void setStatus(String status) { this.status = status; }
} 
package com.hospital.model;

import java.time.LocalDateTime;

public class DietPlan {
    private int id;
    private int patientId;
    private int dieticianId;
    private String planName;
    private String description;
    private String mealPlan;
    private String restrictions;
    private String goals;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private LocalDateTime lastUpdated;

    public DietPlan(int id, int patientId, int dieticianId, String planName, String description, 
                   String mealPlan, String restrictions, String goals, LocalDateTime startDate, 
                   LocalDateTime endDate) {
        this.id = id;
        this.patientId = patientId;
        this.dieticianId = dieticianId;
        this.planName = planName;
        this.description = description;
        this.mealPlan = mealPlan;
        this.restrictions = restrictions;
        this.goals = goals;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "ACTIVE";
        this.lastUpdated = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDieticianId() {
        return dieticianId;
    }

    public void setDieticianId(int dieticianId) {
        this.dieticianId = dieticianId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMealPlan() {
        return mealPlan;
    }

    public void setMealPlan(String mealPlan) {
        this.mealPlan = mealPlan;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "DietPlan{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", dieticianId=" + dieticianId +
                ", planName='" + planName + '\'' +
                ", status='" + status + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
} 
package com.hospital.models;

import java.time.LocalDate;

public class CommunityHealthMetric {
    private int id;
    private String region;
    private String metricName;
    private double value;
    private String unit;
    private LocalDate date;
    private String notes;

    public CommunityHealthMetric(int id, String region, String metricName, double value, String unit, LocalDate date, String notes) {
        this.id = id;
        this.region = region;
        this.metricName = metricName;
        this.value = value;
        this.unit = unit;
        this.date = date;
        this.notes = notes;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
} 
package com.hospital.models;

import java.time.LocalDate;

public class VaccinationDrive {
    private int id;
    private String region;
    private String vaccineName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int targetPopulation;
    private int vaccinated;
    private String status;
    private String notes;

    public VaccinationDrive(int id, String region, String vaccineName, LocalDate startDate, LocalDate endDate, 
                           int targetPopulation, int vaccinated, String status, String notes) {
        this.id = id;
        this.region = region;
        this.vaccineName = vaccineName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetPopulation = targetPopulation;
        this.vaccinated = vaccinated;
        this.status = status;
        this.notes = notes;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getVaccineName() { return vaccineName; }
    public void setVaccineName(String vaccineName) { this.vaccineName = vaccineName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public int getTargetPopulation() { return targetPopulation; }
    public void setTargetPopulation(int targetPopulation) { this.targetPopulation = targetPopulation; }

    public int getVaccinated() { return vaccinated; }
    public void setVaccinated(int vaccinated) { this.vaccinated = vaccinated; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public double getProgress() {
        return (double) vaccinated / targetPopulation * 100;
    }
} 
package com.hospital.models;

public class Community {
    private int id;
    private String name;
    private String location;
    private int population;
    private int managerId;
    private String description;
    private String status;

    public Community(int id, String name, String location, int population, int managerId) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.population = population;
        this.managerId = managerId;
        this.status = "ACTIVE";
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }

    public int getManagerId() { return managerId; }
    public void setManagerId(int managerId) { this.managerId = managerId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return name + " (" + location + ")";
    }
} 
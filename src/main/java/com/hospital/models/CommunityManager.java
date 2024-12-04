package com.hospital.models;

public class CommunityManager extends User {
    private String region;
    private String contactNumber;
    private int populationCovered;
    private boolean available;

    public CommunityManager(int id, String name, String username, int hospitalId, String region, String contactNumber, int populationCovered) {
        super(id, name, username, "COMMUNITY_MANAGER", hospitalId);
        this.region = region;
        this.contactNumber = contactNumber;
        this.populationCovered = populationCovered;
        this.available = true;
    }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public int getPopulationCovered() { return populationCovered; }
    public void setPopulationCovered(int populationCovered) { this.populationCovered = populationCovered; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return getName() + " (" + region + ")";
    }
} 
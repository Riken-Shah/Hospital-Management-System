package com.hospital.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
    private int age;
    private String diagnosis;
    private LocalDate lastVisit;
    private String phone;
    private String address;
    private String bloodGroup;
    private String allergies;
    private LocalDate dateOfBirth;
    private String gender;
    private String emergencyContact;
    private List<String> currentMedications;
    private String email;

    public Patient(int id, String name, String username, int hospitalId) {
        super(id, name, username, "PATIENT", hospitalId);
        this.currentMedications = new ArrayList<>();
    }

    public Patient(int id, String name, int age, String diagnosis, LocalDate lastVisit,
                  String phone, String email, String address, String bloodGroup,
                  String allergies, LocalDate dateOfBirth, String gender,
                  String emergencyContact) {
        super(id, name, email.split("@")[0], "PATIENT", 1); // Default hospital ID
        this.age = age;
        this.diagnosis = diagnosis;
        this.lastVisit = lastVisit;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.bloodGroup = bloodGroup;
        this.allergies = allergies;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.emergencyContact = emergencyContact;
        this.currentMedications = new ArrayList<>();
    }

    // Getters
    public int getAge() { return age; }
    public String getDiagnosis() { return diagnosis; }
    public LocalDate getLastVisit() { return lastVisit; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getBloodGroup() { return bloodGroup; }
    public String getAllergies() { return allergies; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getEmergencyContact() { return emergencyContact; }
    public List<String> getCurrentMedications() { return new ArrayList<>(currentMedications); }
    @Override
    public String getEmail() { return email; }

    // Setters
    public void setAge(int age) { this.age = age; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setLastVisit(LocalDate lastVisit) { this.lastVisit = lastVisit; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setGender(String gender) { this.gender = gender; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    public void setCurrentMedications(List<String> medications) { this.currentMedications = new ArrayList<>(medications); }
    @Override
    public void setEmail(String email) { this.email = email; }
} 
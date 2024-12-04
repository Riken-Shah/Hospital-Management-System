package com.hospital.models;

public class User {
    private int id;
    private String name;
    private String username;
    private String role;
    private int hospitalId;
    private String email;
    private int age;

    public User(int id, String name, String username, String role, int hospitalId) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.role = role;
        this.hospitalId = hospitalId;
        this.email = username + "@hospital.com"; // Default email format
        this.age = 0; // Default age
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public int getHospitalId() { return hospitalId; }
    public String getEmail() { return email; }
    public int getAge() { return age; }

    public void setName(String name) { this.name = name; }
    public void setUsername(String username) { this.username = username; }
    public void setRole(String role) { this.role = role; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }
    public void setEmail(String email) { this.email = email; }
    public void setAge(int age) { this.age = age; }
} 
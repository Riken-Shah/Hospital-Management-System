package com.hospital.models;

public class Staff {
    private int id;
    private int userId;
    private String name;
    private String department;
    private String position;
    private String contactNumber;

    public Staff(int id, int userId, String name, String department, String position, String contactNumber) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.department = department;
        this.position = position;
        this.contactNumber = contactNumber;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
} 
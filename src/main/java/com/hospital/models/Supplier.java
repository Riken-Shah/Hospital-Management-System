package com.hospital.models;

public class Supplier {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String rating;  // A, B, C rating based on performance

    public Supplier(int id, String name, String phone, String email, String rating) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.rating = rating;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getRating() { return rating; }

    // Setters
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setRating(String rating) { this.rating = rating; }
} 
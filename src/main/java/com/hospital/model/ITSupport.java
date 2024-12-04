package com.hospital.model;

public class ITSupport extends BaseEntity {
    private String department;
    private String contactNumber;

    public ITSupport(int id, String name, String username, int hospitalId, String department) {
        super(id, name, username, "IT_SUPPORT", hospitalId);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getITSupportId() {
        return getId();
    }
} 
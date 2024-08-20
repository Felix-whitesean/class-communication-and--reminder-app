package com.felixwhitesean.classcommapp;

public class User {
    private String username, email, phoneNumber, registrationNumber, courseName, department;


    public User() {
        // Default constructor required for Firestore
    }

    public User(String username, String email, String department, String courseName, String registrationNumber, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.department = department;
        this.courseName = courseName;
        this.registrationNumber = registrationNumber;
        this.phoneNumber = phoneNumber;
    }

    //Getters and setters

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}


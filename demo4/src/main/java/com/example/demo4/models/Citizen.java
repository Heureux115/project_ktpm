package com.example.demo4.models;

public class Citizen {
    private int id;
    private String fullName;
    private String relation;
    private String dob;
    private String cccd;
    private String job;
    private Integer householdId;

    private Integer userId;   // NEW — user sở hữu tài khoản

    public Citizen(int id, String fullName, String relation, String dob,
                   String cccd, String job, Integer householdId, Integer userId) {
        this.id = id;
        this.fullName = fullName;
        this.relation = relation;
        this.dob = dob;
        this.cccd = cccd;
        this.job = job;
        this.householdId = householdId;
        this.userId = userId;
    }

    // getters/setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(Integer householdId) {
        this.householdId = householdId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

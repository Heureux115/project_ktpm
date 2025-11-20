package com.example.demo4.models;

import java.util.List;

public class Household {
    private String householdId;
    private String headName;
    private String address;
    private List<Citizen> members; // danh sách nhân khẩu

    public Household(String householdId, String headName, String address, List<Citizen> members) {
        this.householdId = householdId;
        this.headName = headName;
        this.address = address;
        this.members = members;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public List<Citizen> getMembers() {
        return members;
    }

    public void setMembers(List<Citizen> members) {
        this.members = members;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // constructor, getter, setter
}

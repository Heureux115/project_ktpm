package com.example.demo4.models;

import java.util.List;

public class Household {
    private String householdId;
    private String headName;
    private String address;
    private List<Citizen> members;
    private String owner; // chủ hộ / userId quản lý hộ

    // Constructor dùng trong HouseholdCustomerController
    public Household(String householdId, String headName, String address, String owner) {
        this.householdId = householdId;
        this.headName = headName;
        this.address = address;
        this.owner = owner;
    }

    // Nếu sau này có muốn truyền luôn danh sách nhân khẩu
    public Household(String householdId,
                     String headName,
                     String address,
                     List<Citizen> members,
                     String owner) {
        this.householdId = householdId;
        this.headName = headName;
        this.address = address;
        this.members = members;
        this.owner = owner;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

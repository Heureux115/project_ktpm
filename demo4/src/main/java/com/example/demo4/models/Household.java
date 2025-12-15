package com.example.demo4.models;

import java.util.List;

public class Household {
    private int householdId;        // id (PK)
    private Integer headCitizenId;  // id citizen là chủ hộ
    private String address;
    private List<Citizen> members;  // nếu cần load kèm
    private Integer ownerUserId;    // user quản lý hộ

    // Constructor dùng trong HouseholdCustomerController / DAO
    public Household(int householdId, Integer headCitizenId,
                     String address, Integer ownerUserId) {
        this.householdId = householdId;
        this.headCitizenId = headCitizenId;
        this.address = address;
        this.ownerUserId = ownerUserId;
    }

    @Override
    public String toString() {
        return "Hộ " + householdId + " - " + address;
    }

    // getters/setters
    public int getHouseholdId() { return householdId; }
    public void setHouseholdId(int householdId) { this.householdId = householdId; }

    public Integer getHeadCitizenId() { return headCitizenId; }
    public void setHeadCitizenId(Integer headCitizenId) { this.headCitizenId = headCitizenId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<Citizen> getMembers() { return members; }
    public void setMembers(List<Citizen> members) { this.members = members; }

    public Integer getOwnerUserId() { return ownerUserId; }
    public void setOwnerUserId(Integer ownerUserId) { this.ownerUserId = ownerUserId; }
}

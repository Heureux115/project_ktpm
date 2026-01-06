package com.example.demo4.models;

import java.time.LocalDate;

public class CitizenChange {
    private int id;
    private int citizenId;
    private Integer fromHouseholdId; 
    private Integer toHouseholdId;   

    private String changeType;       
    private LocalDate changeDate;
    private String destination;      
    private String note;             

    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCitizenId() { return citizenId; }
    public void setCitizenId(int citizenId) { this.citizenId = citizenId; }
    public Integer getFromHouseholdId() { return fromHouseholdId; }
    public void setFromHouseholdId(Integer fromHouseholdId) { this.fromHouseholdId = fromHouseholdId; }
    public Integer getToHouseholdId() { return toHouseholdId; }
    public void setToHouseholdId(Integer toHouseholdId) { this.toHouseholdId = toHouseholdId; }
    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }
    public LocalDate getChangeDate() { return changeDate; }
    public void setChangeDate(LocalDate changeDate) { this.changeDate = changeDate; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
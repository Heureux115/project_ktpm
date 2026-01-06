package com.example.demo4.models;

public class Event {
    private int id;
    private String title;
    private String date;       
    private String startTime;  
    private String endTime;    
    private String location;
    private String description;
    private String status;

    
    public static final String STATUS_REGISTERED = "DANG KY";
    public static final String STATUS_PAID       = "DA THANH TOAN";
    public static final String STATUS_CONFIRMED  = "XAC NHAN";
    public static final String STATUS_CANCELLED  = "HUY";

    
    public Event(int id, String title, String date,
                 String startTime, String endTime,
                 String location, String description,
                 String status) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = description;
        this.status = status;
    }

    
    
    public Event(int id, String title, String date,
                 String location, String description, String status) {
        this(id, title, date, null, null, location, description, status);
    }

    
    public Event(String title, String date,
                 String startTime, String endTime,
                 String location, String description) {
        this(-1, title, date, startTime, endTime, location, description, STATUS_REGISTERED);
    }

    
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }

    
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDate(String date) { this.date = date; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public void setLocation(String location) { this.location = location; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
}

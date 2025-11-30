package com.example.demo4.models;

public class Event {
    private int id;
    private String title;
    private String date;
    private String location;
    private String description;
    private String status;

    // Các hằng số trạng thái
    public static final String STATUS_REGISTERED = "ĐĂNG KÝ";
    public static final String STATUS_PAID       = "ĐÃ THANH TOÁN";
    public static final String STATUS_CONFIRMED  = "XÁC NHẬN";
    public static final String STATUS_CANCELLED  = "HUỶ";

    public Event(int id, String title, String date,
                 String location, String description, String status) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
        this.status = status;
    }

    public Event(String title, String date,
                 String location, String description) {
        this(-1, title, date, location, description, STATUS_REGISTERED);
    }

    // getters + setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}

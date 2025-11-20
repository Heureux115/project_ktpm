package com.example.demo4.models;

public class Event {
    private int id;
    private String title;
    private String date;
    private String location;
    private String description;

    public Event(int id, String title, String date, String location, String description) {
        this.id=id; this.title=title; this.date=date; this.location=location; this.description=description;
    }
    public Event(String title, String date, String location, String description) {
        this(-1, title, date, location, description);
    }
    // getters + setters
    public int getId(){return id;}
    public String getTitle(){return title;}
    public String getDate(){return date;}
    public String getLocation(){return location;}
    public String getDescription(){return description;}
}

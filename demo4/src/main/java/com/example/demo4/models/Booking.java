package com.example.demo4.models;

public class Booking {
    private int id;
    private int userId;
    private int eventId;
    private String status;
    private String paymentStatus;

    public Booking(int id, int userId, int eventId, String status, String paymentStatus) {
        this.id=id; this.userId=userId; this.eventId=eventId; this.status=status; this.paymentStatus=paymentStatus;
    }
    // getters + setters
    public int getId(){return id;}
    public int getUserId(){return userId;}
    public int getEventId(){return eventId;}
    public String getStatus(){return status;}
    public String getPaymentStatus(){return paymentStatus;}
}

package com.example.demo4.models;

public class User {
    private int id;
    private String username;
    private String password;
    private String role; // CUSTOMER / STAFF / ADMIN
    private String fullname;
    private String email;

    public User(int id, String username, String password, String role, String fullname, String email) {
        this.id = id; this.username = username; this.password = password; this.role = role;
        this.fullname = fullname; this.email = email;
    }
    public User(String username, String password, String role, String fullname, String email) {
        this(-1, username, password, role, fullname, email);
    }
    // getters + setters
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public String getRole(){return role;}
    public String getFullname(){return fullname;}
    public String getEmail(){return email;}
}

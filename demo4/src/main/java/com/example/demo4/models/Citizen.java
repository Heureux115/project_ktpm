package com.example.demo4.models;

public class Citizen {
    private int id;          // Mã định danh duy nhất
    private String fullName; // Họ và tên
    private String relation; // Quan hệ với chủ hộ
    private String dob;      // Ngày tháng năm sinh
    private String cccd;     // CMND/CCCD
    private String job;      // Nghề nghiệp

    // Constructor
    public Citizen(int id, String fullName, String relation, String dob, String cccd, String job) {
        this.id = id;
        this.fullName = fullName;
        this.relation = relation;
        this.dob = dob;
        this.cccd = cccd;
        this.job = job;
    }

    // Getter & Setter
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getRelation() { return relation; }
    public String getDob() { return dob; }
    public String getCccd() { return cccd; }
    public String getJob() { return job; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRelation(String relation) { this.relation = relation; }
    public void setDob(String dob) { this.dob = dob; }
    public void setCccd(String cccd) { this.cccd = cccd; }
    public void setJob(String job) { this.job = job; }
}

package com.starla.mathletics.models;

public class Achievement {
    private int id;
    private int studentId;
    private String badgeName;
    private String earnedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public String getEarnedAt() {
        return earnedAt;
    }

    public void setEarnedAt(String earnedAt) {
        this.earnedAt = earnedAt;
    }
}

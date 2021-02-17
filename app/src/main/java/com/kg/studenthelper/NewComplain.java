package com.kg.studenthelper;

import android.net.Uri;

public class NewComplain {

String text_problem;
String room_problem;
String img_problem;
String key;
String date;
String userId;
String status;
String userName;
String building;
String category;


    public NewComplain() {
    }

    public NewComplain(String building,String text_problem, String room_problem, String img_problem, String key, String date, String userId, String status, String userName,String category) {
        this.building=building;
        this.text_problem = text_problem;
        this.room_problem = room_problem;
        this.img_problem = img_problem;
        this.key = key;
        this.date = date;
        this.userId = userId;
        this.status = status;
        this.userName = userName;
        this.category=category;

    }


    public String getText_problem() {
        return text_problem;
    }

    public String getRoom_problem() {
        return room_problem;
    }

    public String getImg_problem() {
        return img_problem;
    }

    public String getKey() {
        return key;
    }

    public String getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public String getBuilding() {
        return building;
    }

    public String getCategory() {
        return category;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

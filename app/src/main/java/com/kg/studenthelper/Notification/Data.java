package com.kg.studenthelper.Notification;

public class Data {

    private String Title;
    private String Message;
    private String type;
    private String building;

    public Data(String title, String message,String type,String building) {
        Title = title;
        Message = message;
        this.type=type;
        this.building=building;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

}

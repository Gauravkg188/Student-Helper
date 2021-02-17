package com.kg.studenthelper;

public class Notice {

    String content;
    String image;
    String date;
    String key;

    public Notice(){}

    public Notice(String content, String image,String date,String key) {
        this.content = content;
        this.image = image;
        this.date=date;
        this.key=key;
    }


    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    public String getKey() {
        return key;
    }
}

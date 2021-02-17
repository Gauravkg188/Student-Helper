package com.kg.studenthelper;

public class Identity {

    String  Name;
    String type;
    String building;
    String userId;
    String typo;

    public Identity() {
    }

    public String getTypo() {
        return typo;
    }

    public Identity(String name, String type, String building, String userId, String typo) {
        this.Name = name;
        this.type = type;
        this.building = building;
        this.userId=userId;
        this.typo=typo;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getName() {
        return Name;
    }

    public String getType() {
        return this.type;
    }

    public String getBuilding() {
        return this.building;
    }

    public String getUserId() {
        return userId;
    }
}

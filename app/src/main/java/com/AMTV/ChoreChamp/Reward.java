package com.AMTV.ChoreChamp;

public class Reward {
    private String name, description, uid;
    private int points;

//    public Reward(String name, int points, User user, String description) {
    public Reward(){}

    public Reward(String name, int points, String uid, String description) {
        this.points = points;
        this.name = name;
        this.uid = uid;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

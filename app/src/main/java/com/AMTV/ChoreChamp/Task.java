package com.AMTV.ChoreChamp;
import java.util.Date;

public class Task {
    private String name, description, points;
    private String uid;
    private String isComplete;

    public Task() {}
    public Task(String name, String description, String points, String uid) {
        this.name = name;
        this.description = description;
        this.points = points;
        this.isComplete = "false";
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }




    public String getDescription() {
        return description;
    }

    public String getPoints() {
        return points;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setIsComplete(String complete) {
        this.isComplete = complete;
    }
    public String getIsComplete() {
        return isComplete;
    }
}

package com.AMTV.ChoreChamp;
import java.util.Date;

public class Task {
    private String name, description;
    private int points;
    private Date dueDate;
    private User userAssigned;
    private boolean isComplete;
    private Frequency frequency;
//    private enum Frequency {oneTime, Weekly, Monthly};

    public Task(String name, String description, int points, Date dueDate) {
        this.name = name;
        this.description = description;
        this.points = points;
        this.frequency = Frequency.oneTime;
        this.dueDate = dueDate;
        this.userAssigned = null;
        this.isComplete = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPoints() {
        return points;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setUser(User newUser) {
        this.userAssigned = newUser;
    }

    public void setFrequency(Frequency newFrequency) { this.frequency = newFrequency; }

    public void setIsComplete(boolean complete) {
        this.isComplete = complete;
    }
}

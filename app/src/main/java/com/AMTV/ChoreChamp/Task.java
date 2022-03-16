package com.AMTV.ChoreChamp;
import java.util.Date;

public class Task {
    private String name, description;
    private int points;
    private Date dueDate;
    private User userAssigned;
    private boolean is_complete;
    private enum Frequency {oneTime, Weekly, Monthly};

    public Task(String name, String description, int points, Date dueDate, Frequency frequency ) {
        this.name = name;
        this.description = description;
        this.points = points;
        // this.frequency = frequency;
        this.dueDate = dueDate;
        this.userAssigned = null;
        this.is_complete = false;
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
}

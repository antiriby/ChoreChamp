package com.AMTV.ChoreChamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Household implements Serializable {
    private User adminUser;
//    private ArrayList<User> members;
    private HashMap<String,User> members;
    private String householdName,familyPassword;
    private HashMap<String,Reward>availableRewards;
    private HashMap<String,Reward>completedRewards;
    private HashMap<String,Task>availableTasks;
    private HashMap<String,Task>completedTasks;

    private Household() {}

    public Household(String  householdName, String familyPassword, User adminUser) {
        this.householdName = householdName;
        this.familyPassword = familyPassword;
        this.adminUser = adminUser;

        members = new HashMap<>();
        members.put(adminUser.getUid(),adminUser);
    }

    public Household(String  householdName, String familyPassword, User adminUser, HashMap<String,User>members, HashMap<String,Task>availableTasks, HashMap<String,Task>completedTasks, HashMap<String, Reward>availableRewards, HashMap<String, Reward>completedRewards) {
        this.householdName = householdName;
        this.familyPassword = familyPassword;
        this.adminUser = adminUser;
        this.members = members;
        this.availableTasks = availableTasks;
        this.availableRewards = availableRewards;
        this.completedTasks = completedTasks;
        this.completedRewards = completedRewards;
    }


    public void setMembers(HashMap<String,User> members){
        this.members = members;
    }

    public void setAvailableRewards(HashMap<String, Reward> availableRewards) {
        this.availableRewards = availableRewards;
    }

    public void setCompletedRewards(HashMap<String, Reward> completedRewards) {
        this.completedRewards = completedRewards;
    }

    public void setAvailableTasks(HashMap<String, Task> availableTasks) {
        this.availableTasks = availableTasks;
    }

    public void setCompletedTasks(HashMap<String, Task> completedTasks) {
        this.completedTasks = completedTasks;
    }

    public HashMap<String,User> getMembers() {
        return members;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public String getFamilyPassword() {
        return familyPassword;
    }

    public HashMap<String, Reward> getAvailableRewards() {
        return availableRewards;
    }

    public HashMap<String, Reward> getCompletedRewards() {
        return completedRewards;
    }

    public HashMap<String, Task> getAvailableTasks() {
        return availableTasks;
    }

    public HashMap<String, Task> getCompletedTasks() {
        return completedTasks;
    }
}

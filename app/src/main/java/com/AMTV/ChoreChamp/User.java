package com.AMTV.ChoreChamp;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    final int DEFAULT_POINTS = 0;
    private String name, email, role, householdId, uid;
    private int points;

    public User(){}

    public User(String name, String email, Boolean admin, String householdId, String uid) {
        this.name = name;
        this.email = email;
        this.householdId = householdId;
        this.points = DEFAULT_POINTS;
        this.uid = uid;

        if(admin) {
            role = "Admin";
        } else {
            role = "Member";
        }

    }

    public User(String name, String email, String role, String householdId) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.householdId = householdId;
        this.points = DEFAULT_POINTS;
    }


    // Setter Methods
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    // Getter Methods
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getHouseholdId(){
        return householdId;
    }


    public String getUid() { return uid; }

    public void setUid(String uid){
        this.uid = uid;
    }

    public int getPoints() { return points; }
}

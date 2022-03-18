package com.AMTV.ChoreChamp;

import android.media.Image;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    final int DEFAULT_POINTS = 0;
    private String name, email, role, householdId, uid;
    private int points, profileIconId;

    public User(){}

    public User(String name, String email, Boolean admin, String householdId, String uid, int iconId) {
        this.name = name;
        this.email = email;
        this.householdId = householdId;
        this.points = DEFAULT_POINTS;
        this.uid = uid;
        this.profileIconId = iconId;

        if(admin) {
            role = "Admin";
        } else {
            role = "Member";
        }
    }

    public User(String name, String email, Boolean admin, String householdId, String uid) {
        this.name = name;
        this.email = email;
        this.householdId = householdId;
        this.points = DEFAULT_POINTS;
        this.uid = uid;

        if (admin) {
            role = "Admin";
        } else {
            role = "Member";
        }
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

    public void setUid(String uid){
        this.uid = uid;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
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

    public int getPoints() { return points; }

    public int getProfileIconId() {
        return profileIconId;
    }
}

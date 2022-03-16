package com.AMTV.ChoreChamp;

import java.io.Serializable;

public class User implements Serializable {
    private String name, email, role, householdId, uid;

    public User(){}

    public User(String name, String email, Boolean admin, String householdId, String uid) {
        this.name = name;
        this.email = email;
        this.householdId = householdId;
        this.uid = uid;

        if(admin) {
            role = "Admin";
        } else {
            role = "Member";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getHouseholdId(){
        return householdId;
    }

    public void setHouseholdId(String householdId){
        this.householdId = householdId;
    }

    public String getUid() { return uid; }

    public void setUid(String uid){
        this.uid = uid;
    }

}

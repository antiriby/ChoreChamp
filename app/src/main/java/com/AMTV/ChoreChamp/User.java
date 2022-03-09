package com.AMTV.ChoreChamp;

import java.io.Serializable;

public class User implements Serializable {
    private final String name, email, role, householdId;

    public User(String name, String email, Boolean admin, String householdId) {
        this.name = name;
        this.email = email;
        this.householdId = householdId;

        if(admin) {
            role = "Admin";
        } else {
            role = "Member";
        }
    }

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

}

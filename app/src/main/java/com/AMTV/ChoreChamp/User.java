package com.AMTV.ChoreChamp;

public class User {
    private final String name, email, role;

    public User(String name, String email, Boolean admin) {
        this.name = name;
        this.email = email;

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

}

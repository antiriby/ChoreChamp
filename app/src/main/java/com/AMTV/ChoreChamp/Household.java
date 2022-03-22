package com.AMTV.ChoreChamp;

import java.io.Serializable;
import java.util.ArrayList;

public class Household implements Serializable {
    private User adminUser;
    private ArrayList<User> members;
    private String householdName,familyPassword;

    private Household() {}

    public Household(String  householdName, String familyPassword, User adminUser) {
        this.householdName = householdName;
        this.familyPassword = familyPassword;
        this.adminUser = adminUser;

        members = new ArrayList<User>();
        members.add(adminUser);
    }

    public void setMembers(ArrayList<User>members){
        this.members = members;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public String getFamilyPassword() {
        return familyPassword;
    }


}

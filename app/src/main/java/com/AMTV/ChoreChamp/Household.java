package com.AMTV.ChoreChamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Household implements Serializable {
    private User adminUser;
//    private ArrayList<User> members;
    private HashMap<String,User> members;
    private String householdName,familyPassword;
    private int householdSize;

    private Household() {}

    public Household(String  householdName, String familyPassword, User adminUser) {
        this.householdName = householdName;
        this.familyPassword = familyPassword;
        this.adminUser = adminUser;

        members = new HashMap<>();
        members.put(adminUser.getUid(),adminUser);
        householdSize = members.size();
    }

    public void setMembers(HashMap<String,User> members){
        this.members = members;
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


}

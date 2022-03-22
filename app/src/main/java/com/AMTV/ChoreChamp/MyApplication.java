package com.AMTV.ChoreChamp;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

enum Frequency {
    oneTime,
    Weekly,
    Monthly
}

public class MyApplication extends Application {
    private static boolean isAdmin = false;
    private static String userId, householdId, userName = "";
    private static DatabaseReference dbReference;

    public static DatabaseReference getDbReference() {
        return dbReference;
    }

    public static void setDbReference(DatabaseReference dbReference){
        MyApplication.dbReference = dbReference;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        MyApplication.userId = userId;
    }

    public static String getHouseholdId() {
        return householdId;
    }

    public static void setHouseholdId(String householdId) {
        MyApplication.householdId = householdId;
    }


    public static boolean isAdmin() {
        return isAdmin;
    }

    public static void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public static String getUserName() {
        return MyApplication.userName;
    }

    public static void setUserName(String userName) {
        MyApplication.userName = userName;
    }

}

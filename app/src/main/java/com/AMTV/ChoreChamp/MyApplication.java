package com.AMTV.ChoreChamp;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
enum Frequency {
    oneTime,
    Weekly,
    Monthly
}

public class MyApplication extends Application {
    private static boolean isAdmin = false;
    private static String userId, householdId, userName = "";
    private static DatabaseReference dbReference;
    final static int RED_THUMB_ID = R.mipmap.red_thumb_foreground;
    final static int ORANGE_THUMB_ID = R.mipmap.orange_thumb_foreground;
    final static int GOLD_THUMB_ID = R.mipmap.gold_thumb_foreground;
    final static int GREEN_THUMB_ID = R.mipmap.green_thumb_foreground;
    final static int BLUE_THUMB_ID = R.mipmap.blue_thumb_foreground;
    final static int VIOLET_THUMB_ID = R.mipmap.violet_thumb_foreground;

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


    //Icon Color Resource ID's
    public static int getRedThumbId() {
        return RED_THUMB_ID;
    }

    public static int getOrangeThumbId() {
        return ORANGE_THUMB_ID;
    }

    public static int getGoldThumbId() {
        return GOLD_THUMB_ID;
    }

    public static int getGreenThumbId() {
        return GREEN_THUMB_ID;
    }

    public static int getBlueThumbId() {
        return BLUE_THUMB_ID;
    }

    public static int getVioletThumbId() {
        return VIOLET_THUMB_ID;
    }

    public static String getUserName() {
        return MyApplication.userName;
    }

    public static void setUserName(String userName) {
        MyApplication.userName = userName;
    }

}

package com.AMTV.ChoreChamp;

public class Reward {
    private String name;
    private String description;
    private String uid;

    private String rewardId;
    private String points;
    private String claimed;

//    public Reward(String name, int points, User user, String description) {
    public Reward(){}

    public Reward(String name, String points, String uid, String rewardId, String description, String claimed) {
        this.points = points;
        this.name = name;
        this.uid = uid;
        this.description = description;
        this.claimed = claimed;
        this.rewardId = rewardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClaimed(String claimed) {
        this.claimed = claimed;
    }

    public String getClaimed() { return claimed; }

    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(String rewardId) {
        this.rewardId = rewardId;
    }
}

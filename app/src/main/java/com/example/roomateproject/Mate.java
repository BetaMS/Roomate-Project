package com.example.roomateproject;

public class Mate {
    private String userID;
    private int houseID;
    private String splitwiseID;
    private String name;

    public void setUserID(String id){
        this.userID = id;
    }

    public String getUserID(){
        return userID;
    }

    public void setHouseID(int id){
        this.houseID = id;
    }

    public int getHouseID(){
        return houseID;
    }

    public void setSplitWiseID(String id){
        this.splitwiseID = id;
    }

    public String getSplitwiseID(){
        return splitwiseID;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Mate() {
        this.houseID = 0;
    }

    public Mate(String userID, String name){
        this.userID = userID;
        this.name = name;
    }
}

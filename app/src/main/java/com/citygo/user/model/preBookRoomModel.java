package com.citygo.user.model;


public class preBookRoomModel {
    private String uid;
    private int isAvailable;
    private String latLang;

    public preBookRoomModel() {
        // Default constructor required for Firebase
    }

    public preBookRoomModel(String uid, int isAvailable, String latLang) {
        this.uid = uid;
        this.isAvailable = isAvailable;
        this.latLang = latLang;
    }

    // Getters and Setters

    public int getIsAvailable() {
        return isAvailable;
    }

    public String getLatLang() {
        return latLang;
    }

    public void setLatLang(String latLang) {
        this.latLang = latLang;
    }

    public void setIsAvailable(int isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}

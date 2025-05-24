package com.citygo.user.model;

public class RoomModel {
    private String uid;
    private String name;
    private String number;
    private String profileImage;
    private String fcmToken;
    private int isAvailable;
    private String latLang;

    public RoomModel() {
        // Default constructor required for Firebase
    }

    public RoomModel(String uid, String name, String number, String profileImage, String fcmToken, int isAvailable, String latLang) {
        this.uid = uid;
        this.name = name;
        this.number = number;
        this.profileImage = profileImage;
        this.fcmToken = fcmToken;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}

package com.citygo.user.model;



public class FirebaseModel {
    private String name;
    private String number;
    private String profileImage;
    private String fcmToken;
    private String email;
    private String Uid;
    private long timeStamp;
    private String driverId;
    private String latLang;
    private String fromWhere;
    private String toWhere;

    public FirebaseModel() {
        // Default constructor required for calls to DataSnapshot.getValue(FirebaseModel.class)
    }

    public String getLatLang() {
        return latLang;
    }

    public void setLatLang(String latLang) {
        this.latLang = latLang;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public String getToWhere() {
        return toWhere;
    }

    public void setToWhere(String toWhere) {
        this.toWhere = toWhere;
    }

    public FirebaseModel(String name, String number, String profileImage, String fcmToken, String email, String Uid, long timeStamp, String driverId, String latLang, String fromWhere, String toWhere) {
        this.name = name;
        this.number = number;
        this.profileImage = profileImage;
        this.fcmToken = fcmToken;
        this.email = email;
        this.Uid = Uid;
        this.timeStamp = timeStamp;
        this.driverId = driverId;
        this.latLang = latLang;
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String Uid) {
        this.Uid = Uid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
}

package com.citygo.user.model;

public class DriverModel {
    private String number;
    private long timeStamp;
    private String name;
    private String email;
    private String Uid;
    private String isVerified;
    private String profileImage;
    private String FcmToken;
    private String address;
    private String licenceImage;
    private String carImage;
    private String rcNo;
    private String insuranceImage;
    private String carType;
    private Double rating;
    public DriverModel() {
    }

    public DriverModel(String number, long timeStamp, String name, String email, String uid, String isVerified,
                       String profileImage, String FcmToken, String address, String licenceImage, String carImage,
                       String rcNo, String insuranceImage, String carType,Double rating) {
        this.number = number;
        this.timeStamp = timeStamp;
        this.name = name;
        this.email = email;
        this.Uid = uid;
        this.isVerified = isVerified;
        this.profileImage = profileImage;
        this.FcmToken = FcmToken;
        this.address= address;
        this.licenceImage=licenceImage;
        this.carImage=carImage;
        this.rcNo=rcNo;
        this.insuranceImage =insuranceImage;
        this.carType=carType;
        this.rating = rating;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicenceImage() {
        return licenceImage;
    }

    public void setLicenceImage(String licenceImage) {
        this.licenceImage = licenceImage;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getRcNo() {
        return rcNo;
    }

    public void setRcNo(String rcNo) {
        this.rcNo = rcNo;
    }

    public String getInsuranceImage() {
        return insuranceImage;
    }

    public void setInsuranceImage(String insuranceImage) {
        this.insuranceImage = insuranceImage;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getFcmToken() {
        return FcmToken;
    }

    public void setFcmToken(String fcmToken) {
        FcmToken = fcmToken;
    }
}

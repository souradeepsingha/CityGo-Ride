package com.citygo.user.model;

public class advanceBookRVModel {
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
    private String carType,passengerFromWhere,passengerToWhere,passengerLatLang,driverUid;
    private Long pickupTimestamp;
    private Double rating;
    private String node;

    public advanceBookRVModel() {
    }

    public advanceBookRVModel(String number, long timeStamp, String name, String email, String uid, String isVerified,
                              String profileImage, String fcmToken, String address, String licenceImage, String carImage,
                              String rcNo, String insuranceImage, String carType, String passengerFromWhere,
                              String passengerToWhere, String passengerLatLang, String driverUid, Long pickupTimestamp,Double rating,String node) {
        this.number = number;
        this.timeStamp = timeStamp;
        this.name = name;
        this.email = email;
        Uid = uid;
        this.isVerified = isVerified;
        this.profileImage = profileImage;
        FcmToken = fcmToken;
        this.address = address;
        this.licenceImage = licenceImage;
        this.carImage = carImage;
        this.rcNo = rcNo;
        this.insuranceImage = insuranceImage;
        this.carType = carType;
        this.passengerFromWhere = passengerFromWhere;
        this.passengerToWhere = passengerToWhere;
        this.passengerLatLang = passengerLatLang;
        this.driverUid = driverUid;
        this.pickupTimestamp = pickupTimestamp;
        this.rating=rating;
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getPassengerFromWhere() {
        return passengerFromWhere;
    }

    public void setPassengerFromWhere(String passengerFromWhere) {
        this.passengerFromWhere = passengerFromWhere;
    }

    public String getPassengerToWhere() {
        return passengerToWhere;
    }

    public void setPassengerToWhere(String passengerToWhere) {
        this.passengerToWhere = passengerToWhere;
    }

    public String getPassengerLatLang() {
        return passengerLatLang;
    }

    public void setPassengerLatLang(String passengerLatLang) {
        this.passengerLatLang = passengerLatLang;
    }

    public String getDriverUid() {
        return driverUid;
    }

    public void setDriverUid(String driverUid) {
        this.driverUid = driverUid;
    }

    public Long getPickupTimestamp() {
        return pickupTimestamp;
    }

    public void setPickupTimestamp(Long pickupTimestamp) {
        this.pickupTimestamp = pickupTimestamp;
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

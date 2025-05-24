package com.citygo.user.model;

public class userCurrentBookHistoryRVModel {
    private Long TimeStamp;
    private String FromWhere,ToWhere,DriverUid,DriverName,DriverEmail,DriverNumber,DriverRating,DriverJoinDate;

    public userCurrentBookHistoryRVModel() {
    }

    public userCurrentBookHistoryRVModel(Long timeStamp, String fromWhere, String toWhere, String driverUid,
                                         String driverName, String driverEmail, String driverNumber, String driverRating,
                                         String driverJoinDate) {
        TimeStamp = timeStamp;
        FromWhere = fromWhere;
        ToWhere = toWhere;
        DriverUid = driverUid;
        DriverName = driverName;
        DriverEmail = driverEmail;
        DriverNumber = driverNumber;
        DriverRating = driverRating;
        DriverJoinDate = driverJoinDate;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getDriverEmail() {
        return DriverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        DriverEmail = driverEmail;
    }

    public String getDriverNumber() {
        return DriverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        DriverNumber = driverNumber;
    }

    public String getDriverRating() {
        return DriverRating;
    }

    public void setDriverRating(String driverRating) {
        DriverRating = driverRating;
    }

    public String getDriverJoinDate() {
        return DriverJoinDate;
    }

    public void setDriverJoinDate(String driverJoinDate) {
        DriverJoinDate = driverJoinDate;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getFromWhere() {
        return FromWhere;
    }

    public void setFromWhere(String fromWhere) {
        FromWhere = fromWhere;
    }

    public String getToWhere() {
        return ToWhere;
    }

    public void setToWhere(String toWhere) {
        ToWhere = toWhere;
    }

    public String getDriverUid() {
        return DriverUid;
    }

    public void setDriverUid(String driverUid) {
        DriverUid = driverUid;
    }
}

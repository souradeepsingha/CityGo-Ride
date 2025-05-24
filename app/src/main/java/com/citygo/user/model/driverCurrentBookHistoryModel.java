package com.citygo.user.model;

public class driverCurrentBookHistoryModel {
    private Long TimeStamp;
    private String FromWhere,ToWhere,PassengerUid;

    public driverCurrentBookHistoryModel() {
    }

    public driverCurrentBookHistoryModel(Long timeStamp, String fromWhere, String toWhere, String PassengerUid) {
        TimeStamp = timeStamp;
        FromWhere = fromWhere;
        ToWhere = toWhere;
        this.PassengerUid = PassengerUid;
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
        return PassengerUid;
    }

    public String getPassengerUid() {
        return PassengerUid;
    }

    public void setPassengerUid(String passengerUid) {
        PassengerUid = passengerUid;
    }
}

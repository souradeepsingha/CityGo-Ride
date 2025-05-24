package com.citygo.user.model;

import java.sql.Timestamp;

public class preBookReqModel {

    private String passengerLatLang;
    private String passengerFromWhere;
    private String passengerToWhere;
    private Long pickupTimestamp;


    public preBookReqModel(String passengerLatLang, String passengerFromWhere, String passengerToWhere, Timestamp pickupTimestamp) {
        this.passengerLatLang = passengerLatLang;
        this.passengerFromWhere = passengerFromWhere;
        this.passengerToWhere = passengerToWhere;
        this.pickupTimestamp=pickupTimestamp.getTime();

    }

    // Default constructor (required for Firebase)
    public preBookReqModel() {
    }

    // Getter and Setter methods


    public Long getPickupTimestamp() {
        return pickupTimestamp;
    }

    public void setPickupTimestamp(Long pickupTimestamp) {
        this.pickupTimestamp = pickupTimestamp;
    }

    public String getPassengerLatLang() {
        return passengerLatLang;
    }

    public void setPassengerLatLang(String passengerLatLang) {
        this.passengerLatLang = passengerLatLang;
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

}

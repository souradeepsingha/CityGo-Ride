package com.citygo.user.Activities;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDistance {

    @SerializedName("destination_addresses")
    @Expose
    private List<String> destinationAddresses;
    @SerializedName("origin_addresses")
    @Expose
    private List<String> originAddresses;
    @SerializedName("rows")
    @Expose
    private List<Row> rows;
    @SerializedName("status")
    @Expose
    private String status;

    public List<String> getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(List<String> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    public List<String> getOriginAddresses() {
        return originAddresses;
    }

    public void setOriginAddresses(List<String> originAddresses) {
        this.originAddresses = originAddresses;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

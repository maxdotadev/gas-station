package com.hackathon.unknown.gasstation;

/**
 * Created by DELL on 29/10/2016.
 */

public class FavoriteObject {

    String name;
    String address;
    double lat;
    double lng;

    public FavoriteObject(String name, String address, double lat, double lng) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}

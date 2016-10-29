package com.hackathon.unknown.gasstation.network.model;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nguyen Hong Ngoc on 10/29/2016.
 */
public class GooglePlace {
    @SerializedName("place_id")
    private String placeId;
    private String vicinity;
    private String icon;
    private String id;
    private String name;
    private GoogleGeometry geometry;
    private double mDistance; // in meter

    public GoogleGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(GoogleGeometry geometry) {
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private double getDistance() {
        return mDistance;
    }

    public void updateDistance(Location userLocation) {
        Location location = new Location("");
        location.setLatitude(geometry.getLocation().lat);
        location.setLongitude(geometry.getLocation().lng);
        mDistance = userLocation.distanceTo(location);
    }
}

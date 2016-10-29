package com.hackathon.unknown.gasstation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by DELL on 23/01/2016.
 */
public class DirectionStep{
    private String duration;
    private String distance;
    private String instruction;
    private String travelMode;
    private LatLng latLngStart;
    private LatLng latLngEnd;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public LatLng getLatLngStart() {
        return latLngStart;
    }

    public void setLatLngStart(LatLng latLngStart) {
        this.latLngStart = latLngStart;
    }

    public LatLng getLatLngEnd() {
        return latLngEnd;
    }

    public void setLatLngEnd(LatLng latLngEnd) {
        this.latLngEnd = latLngEnd;
    }

}

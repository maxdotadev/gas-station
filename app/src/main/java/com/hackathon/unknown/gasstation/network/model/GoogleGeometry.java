package com.hackathon.unknown.gasstation.network.model;

/**
 * Created by Nguyen Hong Ngoc on 10/29/2016.
 */

public class GoogleGeometry {
    private GoogleLocation location;

    public GoogleLocation getLocation() {
        return location;
    }

    public void setLocation(GoogleLocation location) {
        this.location = location;
    }

    public class GoogleLocation {
        public double lat;
        public double lng;
    }
}

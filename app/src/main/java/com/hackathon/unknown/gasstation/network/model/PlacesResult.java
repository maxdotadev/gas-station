package com.hackathon.unknown.gasstation.network.model;

import java.util.ArrayList;

/**
 * Created by Nguyen Hong Ngoc on 10/29/2016.
 */

public class PlacesResult {
    private ArrayList<GooglePlace> results;

    public ArrayList<GooglePlace> getResults() {
        return results;
    }

    public void setResult(ArrayList<GooglePlace> results) {
        this.results = results;
    }
}

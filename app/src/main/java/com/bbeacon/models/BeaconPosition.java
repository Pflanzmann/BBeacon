package com.bbeacon.models;

public class BeaconPosition {

    private float x;
    private float y;

    private String beaconId;

    public BeaconPosition(float x, float y, String beaconId) {
        this.x = x;
        this.y = y;
        this.beaconId = beaconId;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public String getBeaconId() {
        return beaconId;
    }
}

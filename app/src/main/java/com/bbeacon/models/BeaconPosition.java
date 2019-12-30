package com.bbeacon.models;

public class BeaconPosition implements Beacon {

    private float x;
    private float y;

    private String deviceId;

    public BeaconPosition(float x, float y, String beaconId) {
        this.x = x;
        this.y = y;
        this.deviceId = beaconId;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String getMacAddress() {
        return null;
    }


}

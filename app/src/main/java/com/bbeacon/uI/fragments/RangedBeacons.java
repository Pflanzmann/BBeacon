package com.bbeacon.uI.fragments;

import com.bbeacon.models.Beacon;

public class RangedBeacons implements Beacon {

    private String macAddress;
    private String deviceName;

    @Override
    public String getMacAddress() {
        return null;
    }

    @Override
    public String getDeviceId() {
        return null;
    }
}

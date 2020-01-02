package com.bbeacon.models;

import java.io.Serializable;

public class Beacon implements Serializable {

    public Beacon(String deviceId, String macAddress) {
        this.deviceId = deviceId;
        this.macAddress = macAddress;
    }

    private String deviceId;
    private String macAddress;

    public String getDeviceId() {
        return deviceId;
    }

    public String getMacAddress() {
        return macAddress;
    }
}

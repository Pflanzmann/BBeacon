package com.bbeacon.models;

import java.io.Serializable;

public class Beacon implements Serializable {

    public Beacon(String deviceId, String macAddress, int txPower) {
        this.deviceId = deviceId;
        this.macAddress = macAddress;
        this.txPower = txPower;
    }

    private String deviceId;
    private String macAddress;
    private int txPower;

    public String getDeviceId() {
        return deviceId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public int getTxPower() {
        return txPower;
    }
}

package com.bbeacon.models;

public class BleScanResult {

    private String macAddress;
    private int rssi;
    private String deviceName;

    public String getMacAddress() {
        return macAddress;
    }

    public int getRssi() {
        return rssi;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public BleScanResult(String macAddress, int rssi, String deviceName) {
        this.macAddress = macAddress;
        this.rssi = rssi;
        this.deviceName = deviceName;
    }
}

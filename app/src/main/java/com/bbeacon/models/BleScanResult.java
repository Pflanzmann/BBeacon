package com.bbeacon.models;

public class BleScanResult {

    private String macAddress;
    private int rssi;
    private String deviceName;
    private int txPower;

    public String getMacAddress() {
        return macAddress;
    }

    public int getRssi() {
        return rssi;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getTxPower() {
        return txPower;
    }

    public BleScanResult(String macAddress, int rssi, String deviceName, int txPower) {
        this.macAddress = macAddress;
        this.rssi = rssi;
        this.deviceName = deviceName;
        this.txPower = txPower;
    }
}

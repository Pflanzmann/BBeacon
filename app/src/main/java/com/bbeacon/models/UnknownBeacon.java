package com.bbeacon.models;

import java.io.Serializable;

import androidx.annotation.Nullable;

public class UnknownBeacon implements Serializable {

    public UnknownBeacon(String macAddress, String deviceName, String distance, String rssi) {
        this.macAddress = macAddress;
        this.deviceName = deviceName;
        this.distance = distance;
        this.rssi = rssi;
    }

    private String macAddress;
    private String deviceName;
    private String distance;
    private String rssi;

    public String getDistance() {
        return distance;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }

        if (this.deviceName.contentEquals(((UnknownBeacon) obj).deviceName)
                && this.macAddress.contentEquals(((UnknownBeacon) obj).macAddress))
            return true;

        return false;
    }

    public void setDistamce(String distance) {
        this.distance = distance;
    }
}

package com.bbeacon.models;

import java.io.Serializable;

import androidx.annotation.Nullable;

public class UnknownBeacon implements Serializable {

    private String macAddress;
    private String deviceName;

    private String rssi;

    public UnknownBeacon(String macAddress, String deviceName, String rssi) {
        this.macAddress = macAddress;
        this.deviceName = deviceName;
        this.rssi = rssi;
    }

    public UnknownBeacon(String macAddress, String deviceName) {
        this.macAddress = macAddress;
        this.deviceName = deviceName;
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
}

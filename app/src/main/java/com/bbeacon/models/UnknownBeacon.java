package com.bbeacon.models;

import androidx.annotation.Nullable;

public class UnknownBeacon implements Beacon {

    private final String macAddress;
    private final String deviceName;

    public UnknownBeacon(String macAddress, String deviceName) {
        this.macAddress = macAddress;
        this.deviceName = deviceName;
    }

    @Override
    public String getMacAddress() {
        return macAddress;
    }

    @Override
    public String getDeviceName() {
        return deviceName;
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

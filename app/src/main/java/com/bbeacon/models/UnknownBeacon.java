package com.bbeacon.models;

<<<<<<< HEAD
import androidx.annotation.Nullable;

public class UnknownBeacon {
    public String getMacAddress() {
        return macAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    private final String macAddress;
    private final String deviceName;

    public UnknownBeacon(String macAddress, String deviceName) {
        this.macAddress = macAddress;
        this.deviceName = deviceName;
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
=======
public class UnknownBeacon {
>>>>>>> refs/remotes/origin/master
}

package com.bbeacon.models;

import java.io.Serializable;

public class UncalibratedBeacon implements Beacon, Serializable {

    private String macAddress;
    private String deviceName;
    private String deviceId;
    private int meassurementCount;
    private int calibrationSteps;

    public String getDeviceId() {
        return deviceId;
    }

    public int getCalibrationSteps() {
        return calibrationSteps;
    }

    public int getMeassurementCount() {
        return meassurementCount;
    }

    public UncalibratedBeacon(String macAddress, String deviceName, String deviceId, int calibrationSteps, int meassurementCount) {
        this.macAddress = macAddress;
        this.deviceName = deviceName;
        this.deviceId = deviceId;
        this.calibrationSteps = calibrationSteps;
        this.meassurementCount = meassurementCount;
    }


    @Override
    public String getMacAddress() {
        return macAddress;
    }

    @Override
    public String getDeviceName() {
        return deviceName;
    }
}

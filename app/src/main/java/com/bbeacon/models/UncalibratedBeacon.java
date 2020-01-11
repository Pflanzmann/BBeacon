package com.bbeacon.models;

import java.io.Serializable;

public class UncalibratedBeacon extends Beacon implements Serializable {

    private String deviceName;
    private int measurementCount;
    private int calibrationSteps;

    public UncalibratedBeacon(String deviceId, String macAddress, String deviceName, int measurementCount, int calibrationSteps, int txPower) {
        super(deviceId, macAddress, txPower);
        this.deviceName = deviceName;
        this.measurementCount = measurementCount;
        this.calibrationSteps = calibrationSteps;
    }

    public UncalibratedBeacon(Beacon beacon, String deviceName, int measurementCount, int calibrationSteps) {
        super(beacon.getDeviceId(), beacon.getMacAddress(), beacon.getTxPower());
        this.deviceName = deviceName;
        this.measurementCount = measurementCount;
        this.calibrationSteps = calibrationSteps;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getCalibrationSteps() { return calibrationSteps; }

    public int getMeasurementCount() {
        return measurementCount;
    }

}

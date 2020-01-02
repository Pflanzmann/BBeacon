package com.bbeacon.models;

import java.io.Serializable;

public class PositionedBeacon extends CalibratedBeacon implements Serializable {

    private float x;
    private float y;

    public PositionedBeacon(CalibratedBeacon calibratedBeacon, float x, float y) {
        super(calibratedBeacon.getDeviceId(),
                calibratedBeacon.getMacAddress(),
                calibratedBeacon.getDeviceName(),
                calibratedBeacon.getMeasurementCount(),
                calibratedBeacon.getCalibrationSteps(),
                calibratedBeacon.getCalibrationDate(),
                calibratedBeacon.getDataSets());
        this.x = x;
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }
}

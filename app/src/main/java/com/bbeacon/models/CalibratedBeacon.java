package com.bbeacon.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class CalibratedBeacon extends UncalibratedBeacon implements Serializable {

    private Date calibrationDate;

    private ArrayList<RawDataSet<Integer>> dataSets;

    public CalibratedBeacon(String deviceId, String macAddress, String deviceName, int measurementCount, int calibrationSteps, Date calibrationDate, ArrayList<RawDataSet<Integer>> dataSets) {
        super(deviceId, macAddress, deviceName, measurementCount, calibrationSteps);
        this.calibrationDate = calibrationDate;
        this.dataSets = dataSets;
    }

    public CalibratedBeacon(UncalibratedBeacon uncalibratedBeacon, Date calibrationDate, ArrayList<RawDataSet<Integer>> dataSets) {
        super(uncalibratedBeacon.getDeviceId(),
                uncalibratedBeacon.getMacAddress(),
                uncalibratedBeacon.getDeviceName(),
                uncalibratedBeacon.getMeasurementCount(),
                uncalibratedBeacon.getCalibrationSteps());

        this.calibrationDate = calibrationDate;
        this.dataSets = dataSets;
    }

    public Date getCalibrationDate() {
        return calibrationDate;
    }

    public ArrayList<RawDataSet<Integer>> getDataSets() {
        return dataSets;
    }
}

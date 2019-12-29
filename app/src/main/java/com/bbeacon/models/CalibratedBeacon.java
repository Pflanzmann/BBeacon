package com.bbeacon.models;

import java.util.ArrayList;
import java.util.Date;

public class CalibratedBeacon implements Beacon {

    private String deviceName;
    private String deviceId;
    private String macAddress;
    private Date calibrationDate;

    private ArrayList<RawDataSet<Integer>> dataSets;

    public CalibratedBeacon(String deviceName, String deviceId, String macAddress, Date calibrationDate, ArrayList<RawDataSet<Integer>> dataSets) {
        this.deviceName = deviceName;
        this.deviceId = deviceId;
        this.macAddress = macAddress;
        this.calibrationDate = calibrationDate;
        this.dataSets = dataSets;
    }

    @Override
    public String getMacAddress() {
        return macAddress;
    }

    @Override
    public String getDeviceName() {
        return deviceName;
    }

    public Date getCalibrationDate() {
        return calibrationDate;
    }

    public ArrayList<RawDataSet<Integer>> getDataSets() {
        return dataSets;
    }

    public String getDeviceId() {
        return deviceId;
    }
}

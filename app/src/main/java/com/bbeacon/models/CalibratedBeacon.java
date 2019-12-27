package com.bbeacon.models;

import java.util.ArrayList;
import java.util.Date;

public class CalibratedBeacon implements Beacon {

    private String name;
    private String macAddress;
    private Date calibrationDate;

    private ArrayList<RawDataSet<Integer>> dataSets = new ArrayList<>();

    public CalibratedBeacon(String name, String macAddress, Date calibrationDate, ArrayList<RawDataSet<Integer>> dataSets) {
        this.name = name;
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
        return name;
    }

    public Date getCalibrationDate() {
        return calibrationDate;
    }

    public ArrayList<RawDataSet<Integer>> getDataSets() {
        return dataSets;
    }
}

package com.bbeacon.models;

import java.util.ArrayList;

public class CalibratedBeacon implements Beacon {

    private String name;
    private String macAddress;

    private ArrayList<RawDataSet<Integer>> dataSets = new ArrayList<>();

    public CalibratedBeacon(String name, String macAddress, ArrayList<RawDataSet<Integer>> dataSets) {
        this.name = name;
        this.macAddress = macAddress;
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

    public ArrayList<RawDataSet<Integer>> getDataSets(){
        return dataSets;
    }
}

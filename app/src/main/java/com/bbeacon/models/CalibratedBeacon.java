package com.bbeacon.models;

import java.util.ArrayList;

public class CalibratedBeacon {

    public CalibratedBeacon(String name, String macAddress, ArrayList<RawDataSet<Integer>> dataSets) {
        this.name = name;
        this.macAddress = macAddress;
        this.dataSets = dataSets;
    }

    private String name;
    private String macAddress;

    private ArrayList<RawDataSet<Integer>> dataSets = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getMacAddress() {
        return macAddress;
    }
}

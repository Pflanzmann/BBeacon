package com.bbeacon.models;

public class BeaconDataBundle {

    private RawDataSet<Integer>[] dataBundle;


    public BeaconDataBundle(RawDataSet<Integer>[] dataBundle) {
        this.dataBundle = dataBundle;
    }

    public RawDataSet<Integer>[] getDataBundle() {
        return dataBundle;
    }
}

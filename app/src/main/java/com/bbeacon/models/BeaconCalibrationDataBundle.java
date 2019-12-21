package com.bbeacon.models;

public class BeaconCalibrationDataBundle {

    private RawDataSet<Integer>[] dataBundle;


    public BeaconCalibrationDataBundle(RawDataSet<Integer>[] dataBundle) {
        this.dataBundle = dataBundle;
    }

    public RawDataSet<Integer>[] getDataBundle() {
        return dataBundle;
    }
}

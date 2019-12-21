package com.bbeacon.backend;

import com.bbeacon.models.RawDataSet;

import java.util.ArrayList;

public class BeaconCalibrationEvaluator {

    private ArrayList<RawDataSet<Integer>> dataSets = new ArrayList<>();

    public void insertRawDataSet(RawDataSet<Integer> dataSet){
        dataSets.add(dataSet);
    }
}

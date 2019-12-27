package com.bbeacon.backend;

import android.util.Log;

import com.bbeacon.managers.Storage.BeaconStorageManagerType;
import com.bbeacon.models.CalibratedBeacon;
import com.bbeacon.models.RawDataSet;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

public class Evaluator implements EvaluatorType {

    private ArrayList<RawDataSet<Integer>> dataSets = new ArrayList<>();

    BeaconStorageManagerType storageManager;

    @Inject
    public Evaluator(BeaconStorageManagerType storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public void insertRawDataSet(RawDataSet<Integer> dataSet) {
        dataSets.add(dataSet);
    }

    @Override
    public void evaluateAndFinish(String name, String macAddress) {
        Log.d("OwnLog", "evaluateAndFinish: That many datasets: " + dataSets.size());
        storageManager.storeBeacon(new CalibratedBeacon(name, macAddress, new Date() , dataSets));
    }

    private int average(RawDataSet<Integer> dataSet) {
        int a = 0;

        for (Integer data : dataSet.getSet())
            a += data;

        return a / 5;
    }
}

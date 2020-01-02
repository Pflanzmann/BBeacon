package com.bbeacon.backend;

import android.util.Log;

import com.bbeacon.models.CalibratedBeacon;
import com.bbeacon.models.RawDataSet;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

public class AverageRanger implements RangerType {

    CalibratedBeacon beacon;

    int[] averagedDataSets;

    @Inject
    public AverageRanger(CalibratedBeacon beacon) {
        this.beacon = beacon;

        setAverageDatas();
    }

    @Override
    public float computeDistance(int rssi) {
        Log.d("OwnLog", "computeDistance RSSi: " + rssi);
        int i = 0;

        for (; i < averagedDataSets.length; i++) {
            if (averagedDataSets[i] < rssi) {
                Log.d("OwnLog", "computeDistance return: " + i);
                if (averagedDataSets.length > i + 1)
                    if (rssi - averagedDataSets[i] > rssi - averagedDataSets[i + 1])
                        return i + 1;
                return i;
            }
        }
        return i;
    }


    private int[] setAverageDatas() {
        ArrayList<RawDataSet<Integer>> dataSets = beacon.getDataSets();

        averagedDataSets = new int[dataSets.size()];
        int currentDataSet = 0;

        for (RawDataSet<Integer> dataSet : dataSets) {

            Integer[] tempDataSet = dataSet.getSet();
            Arrays.sort(tempDataSet);

            int a = 0;
            for (int j = 0; j < tempDataSet.length; j++) {
                Log.d("OwnLog", "tempDataSet " + currentDataSet + ": " + tempDataSet[j]);

                a += tempDataSet[j];
            }

            int tempAverage = (a / tempDataSet.length);

            a = 0;
            for (int j = 0; j < tempDataSet.length; j++) {
                int difference = Math.abs(tempDataSet[j] - tempAverage);
                if (difference > Math.abs(tempAverage / 10))
                    tempDataSet[j] = tempAverage;

                a += tempDataSet[j];
            }

            averagedDataSets[currentDataSet++] = (a / tempDataSet.length);
        }

        for (int i = 0; i < averagedDataSets.length; i++)
            Log.d("OwnLog", "setAverageDatas: " + averagedDataSets[i]);

        return averagedDataSets;
    }
}

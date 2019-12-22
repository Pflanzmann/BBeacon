package com.bbeacon.backend;

import android.util.Log;

import com.bbeacon.models.RawDataSet;

import java.util.ArrayList;

public class Evaluator {

    private ArrayList<RawDataSet<Integer>> dataSets = new ArrayList<>();

    public void insertRawDataSet(RawDataSet<Integer> dataSet) {
        dataSets.add(dataSet);

        Log.d("OwnLog", String.valueOf(dataSets));
    }

    public void printAll() {
        for (RawDataSet<Integer> dataSet : dataSets)
            Log.d("OwnLog", "Sets: " + dataSet.getSet() + " Average: " + average(dataSet));
    }

    private int average(RawDataSet<Integer> dataSet) {
        int a = 0;

        for (Integer data : dataSet.getSet())
            a += data;

        return a / 5;
    }
}

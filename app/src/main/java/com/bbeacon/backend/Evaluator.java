package com.bbeacon.backend;

import com.bbeacon.exceptions.DataSetDoesNotFitException;
import com.bbeacon.models.CalibratedBeacon;
import com.bbeacon.models.RawDataSet;
import com.bbeacon.models.UncalibratedBeacon;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

public class Evaluator implements EvaluatorType {

    private ArrayList<RawDataSet<Integer>> dataSets = new ArrayList<>();
    private int lastSimpleAverage = 0;

    @Inject
    public Evaluator() {
    }

    @Override
    public void insertRawDataSet(RawDataSet<Integer> dataSet) throws DataSetDoesNotFitException {
        int currentSimpleAverage = average(dataSet);

        if (currentSimpleAverage > lastSimpleAverage)
            throw new DataSetDoesNotFitException("DataSet is invalid and does not fit.");

        dataSets.add(dataSet);
        lastSimpleAverage = currentSimpleAverage;
    }

    @Override
    public CalibratedBeacon evaluateAndFinish(UncalibratedBeacon uncalibratedBeacon) {
        return new CalibratedBeacon(uncalibratedBeacon, new Date(), dataSets);
    }

    private int average(RawDataSet<Integer> dataSet) {
        int a = 0;

        for (Integer data : dataSet.getSet())
            a += data;

        return a / 5;
    }
}

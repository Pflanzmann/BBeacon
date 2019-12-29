package com.bbeacon.backend;

import com.bbeacon.models.RawDataSet;
import com.bbeacon.models.UncalibratedBeacon;

public interface EvaluatorType {
    void insertRawDataSet(RawDataSet<Integer> dataSet);

    void evaluateAndFinish(UncalibratedBeacon uncalibratedBeacon);
}

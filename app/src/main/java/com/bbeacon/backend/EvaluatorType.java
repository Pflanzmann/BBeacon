package com.bbeacon.backend;

import com.bbeacon.exceptions.DataSetDoesNotFitException;
import com.bbeacon.models.RawDataSet;
import com.bbeacon.models.UncalibratedBeacon;

public interface EvaluatorType {
    void insertRawDataSet(RawDataSet<Integer> dataSet) throws DataSetDoesNotFitException;

    void evaluateAndFinish(UncalibratedBeacon uncalibratedBeacon);
}

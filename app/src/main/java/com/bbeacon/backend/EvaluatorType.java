package com.bbeacon.backend;

import com.bbeacon.models.RawDataSet;

public interface EvaluatorType {
    void insertRawDataSet(RawDataSet<Integer> dataSet);

    void evaluateAndFinish(String name, String macAddress);
}

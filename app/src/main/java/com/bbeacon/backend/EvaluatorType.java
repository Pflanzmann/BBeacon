package com.bbeacon.backend;

import com.bbeacon.models.RawDataSet;

public interface EvaluatorType {
    public void insertRawDataSet(RawDataSet<Integer> dataSet);

    public void printAll();
}

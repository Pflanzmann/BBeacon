package com.bbeacon.backend;

import com.bbeacon.exceptions.DataSetDoesNotFitException;
import com.bbeacon.models.CalibratedBeacon;
import com.bbeacon.models.RawDataSet;
import com.bbeacon.models.UncalibratedBeacon;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EvaluatorTest {

    private Evaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new Evaluator();
    }

    @AfterEach
    void tearDown() {
        evaluator = null;
    }

    @Test
    void insertRawDataSet_Success() {
        final int testKey = 0;
        final Integer[] testSet1 = {-1, -1, -1, -1};
        final Integer[] testSet2 = {-2, -2, -2, -2};
        final Integer[] testSet3 = {-3, -3, -3, -3};

        RawDataSet<Integer> testDataSet1 = new RawDataSet<Integer>(testKey, testSet1);
        RawDataSet<Integer> testDataSet2 = new RawDataSet<Integer>(testKey, testSet2);
        RawDataSet<Integer> testDataSet3 = new RawDataSet<Integer>(testKey, testSet3);

        Assertions.assertDoesNotThrow(() -> {
            evaluator.insertRawDataSet(testDataSet1);
            evaluator.insertRawDataSet(testDataSet2);
            evaluator.insertRawDataSet(testDataSet3);
        });
    }

    @Test
    void insertRawDataSet_ThrowDataSetDoesNotFitException() {
        final int testKey = 0;
        final Integer[] testSet1 = {-1, -1, -1, -1};
        final Integer[] testSet3 = {-3, -3, -3, -3};

        RawDataSet<Integer> testDataSet1 = new RawDataSet<Integer>(testKey, testSet1);
        RawDataSet<Integer> testDataSet3 = new RawDataSet<Integer>(testKey, testSet3);

        Assertions.assertThrows(DataSetDoesNotFitException.class, () -> {
            evaluator.insertRawDataSet(testDataSet3);
            evaluator.insertRawDataSet(testDataSet1);
        });
    }

    @Test
    void insertRawDataSet_ThrowDataSetDoesNotFitException_PositivValues() {
        final int testKey = 0;
        final Integer[] testSet1 = {2, 2, 2, 2};

        RawDataSet<Integer> testDataSet1 = new RawDataSet<Integer>(testKey, testSet1);

        Assertions.assertThrows(DataSetDoesNotFitException.class, () -> {
            evaluator.insertRawDataSet(testDataSet1);
        });
    }

    @Test
    void evaluateAndFinish_Success_OneDataSet() {
        final int testKey = 0;
        final Integer[] testSet1 = {1, 1, 1, 1};
        RawDataSet<Integer> testDataSet1 = new RawDataSet<Integer>(testKey, testSet1);

        UncalibratedBeacon testBeacon = new UncalibratedBeacon("deviceId", "macAddress", "deviceName", 42, 42, -60);

        Assertions.assertDoesNotThrow(() -> {
            evaluator.insertRawDataSet(testDataSet1);
        });

        CalibratedBeacon resultBeacon = evaluator.evaluateAndFinish(testBeacon);

        Assert.assertEquals(testBeacon.getDeviceId(), resultBeacon.getDeviceId());
        Assert.assertEquals(testBeacon.getMacAddress(), resultBeacon.getMacAddress());
        Assert.assertEquals(testBeacon.getDeviceName(), resultBeacon.getDeviceName());
        Assert.assertEquals(testBeacon.getMeasurementCount(), resultBeacon.getMeasurementCount());
        Assert.assertEquals(testBeacon.getCalibrationSteps(), resultBeacon.getCalibrationSteps());
        Assert.assertEquals(1, resultBeacon.getDataSets().size());
    }

    @Test
    void evaluateAndFinish_Success_TwoDataSet() {
        final int testKey = 0;
        final Integer[] testSet1 = {-1, -1, -1, -1};
        final Integer[] testSet2 = {-2, -2, -2, -2};
        final Integer[] testSet3 = {-3, -3, -3, -3};

        RawDataSet<Integer> testDataSet1 = new RawDataSet<Integer>(testKey, testSet1);
        RawDataSet<Integer> testDataSet2 = new RawDataSet<Integer>(testKey, testSet2);
        RawDataSet<Integer> testDataSet3 = new RawDataSet<Integer>(testKey, testSet3);

        UncalibratedBeacon testBeacon = new UncalibratedBeacon("deviceId", "macAddress", "deviceName", 42, 42, -60);


        Assertions.assertDoesNotThrow(() -> {
            evaluator.insertRawDataSet(testDataSet1);
            evaluator.insertRawDataSet(testDataSet2);
            evaluator.insertRawDataSet(testDataSet3);
        });

        CalibratedBeacon resultBeacon = evaluator.evaluateAndFinish(testBeacon);

        Assert.assertEquals(testBeacon.getDeviceId(), resultBeacon.getDeviceId());
        Assert.assertEquals(testBeacon.getMacAddress(), resultBeacon.getMacAddress());
        Assert.assertEquals(testBeacon.getDeviceName(), resultBeacon.getDeviceName());
        Assert.assertEquals(testBeacon.getMeasurementCount(), resultBeacon.getMeasurementCount());
        Assert.assertEquals(testBeacon.getCalibrationSteps(), resultBeacon.getCalibrationSteps());
        Assert.assertEquals(3, resultBeacon.getDataSets().size());
    }
}
package com.bbeacon.backend;

import com.bbeacon.exceptions.DataSetDoesNotFitException;
import com.bbeacon.managers.storage.BeaconStorageManager;
import com.bbeacon.models.CalibratedBeacon;
import com.bbeacon.models.RawDataSet;
import com.bbeacon.models.UncalibratedBeacon;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

class EvaluatorTest {

    private Evaluator evaluator;
    private BeaconStorageManager mockBeaconStorageManager;

    @BeforeEach
    void setUp() {
        mockBeaconStorageManager = mock(BeaconStorageManager.class);
        evaluator = new Evaluator(mockBeaconStorageManager);
    }

    @AfterEach
    void tearDown() {
        mockBeaconStorageManager = null;
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
        verifyZeroInteractions(mockBeaconStorageManager);
    }

    @Test
    void insertRawDataSet_ThrowDataSetDoesNotFitException_PositivValues() {
        final int testKey = 0;
        final Integer[] testSet1 = {2, 2, 2, 2};

        RawDataSet<Integer> testDataSet1 = new RawDataSet<Integer>(testKey, testSet1);

        Assertions.assertThrows(DataSetDoesNotFitException.class, () -> {
            evaluator.insertRawDataSet(testDataSet1);
        });
        verifyZeroInteractions(mockBeaconStorageManager);
    }

    @Test
    void evaluateAndFinish_Success_OneDataSet() {
        final int testKey = 0;
        final Integer[] testSet1 = {1, 1, 1, 1};
        RawDataSet<Integer> testDataSet1 = new RawDataSet<Integer>(testKey, testSet1);

        UncalibratedBeacon testBeacon = new UncalibratedBeacon("deviceId", "macAddress", "deviceName", 42, 42);

        ArgumentCaptor<CalibratedBeacon> beaconArgumentCaptor = ArgumentCaptor.forClass(CalibratedBeacon.class);

        Assertions.assertDoesNotThrow(() -> {
            evaluator.insertRawDataSet(testDataSet1);
        });

        evaluator.evaluateAndFinish(testBeacon);

        verify(mockBeaconStorageManager, times(1)).storeBeacon(beaconArgumentCaptor.capture());

        CalibratedBeacon beaconResult = beaconArgumentCaptor.getValue();

        Assert.assertEquals(testBeacon.getDeviceId(), beaconResult.getDeviceId());
        Assert.assertEquals(testBeacon.getMacAddress(), beaconResult.getMacAddress());
        Assert.assertEquals(testBeacon.getDeviceName(), beaconResult.getDeviceName());
        Assert.assertEquals(testBeacon.getMeasurementCount(), beaconResult.getMeasurementCount());
        Assert.assertEquals(testBeacon.getCalibrationSteps(), beaconResult.getCalibrationSteps());
        Assert.assertEquals(1, beaconResult.getDataSets().size());
        verifyNoMoreInteractions(mockBeaconStorageManager);
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

        UncalibratedBeacon testBeacon = new UncalibratedBeacon("deviceId", "macAddress", "deviceName", 42, 42);

        ArgumentCaptor<CalibratedBeacon> beaconArgumentCaptor = ArgumentCaptor.forClass(CalibratedBeacon.class);

        Assertions.assertDoesNotThrow(() -> {
            evaluator.insertRawDataSet(testDataSet1);
            evaluator.insertRawDataSet(testDataSet2);
            evaluator.insertRawDataSet(testDataSet3);
        });

        evaluator.evaluateAndFinish(testBeacon);

        verify(mockBeaconStorageManager, times(1)).storeBeacon(beaconArgumentCaptor.capture());

        CalibratedBeacon beaconResult = beaconArgumentCaptor.getValue();

        Assert.assertEquals(testBeacon.getDeviceId(), beaconResult.getDeviceId());
        Assert.assertEquals(testBeacon.getMacAddress(), beaconResult.getMacAddress());
        Assert.assertEquals(testBeacon.getDeviceName(), beaconResult.getDeviceName());
        Assert.assertEquals(testBeacon.getMeasurementCount(), beaconResult.getMeasurementCount());
        Assert.assertEquals(testBeacon.getCalibrationSteps(), beaconResult.getCalibrationSteps());
        Assert.assertEquals(3, beaconResult.getDataSets().size());
        verifyNoMoreInteractions(mockBeaconStorageManager);
    }
}
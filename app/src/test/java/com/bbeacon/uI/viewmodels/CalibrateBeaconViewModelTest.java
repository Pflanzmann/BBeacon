package com.bbeacon.uI.viewmodels;

import com.bbeacon.backend.Evaluator;
import com.bbeacon.exceptions.DataSetDoesNotFitException;
import com.bbeacon.exceptions.ScanFilterInvalidException;
import com.bbeacon.managers.BleManager;
import com.bbeacon.models.BleScanResult;
import com.bbeacon.models.RawDataSet;
import com.bbeacon.models.UncalibratedBeacon;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import io.reactivex.rxjava3.subjects.PublishSubject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CalibrateBeaconViewModelTest {


    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private BleManager mockBleManager;
    private Evaluator mockEvaluator;
    private CalibrateBeaconViewModel calibrateBeaconViewModel;

    @Before
    public void setUp() {

        mockBleManager = mock(BleManager.class);
        mockEvaluator = mock(Evaluator.class);
        calibrateBeaconViewModel = new CalibrateBeaconViewModel(mockBleManager, mockEvaluator);
    }

    @After
    public void tearDown() {
        mockBleManager = null;
        mockEvaluator = null;
        calibrateBeaconViewModel = null;
    }

    @Test
    public void calibrate_SoloStep_GoThrough_WithNotChangingStep() throws ScanFilterInvalidException, DataSetDoesNotFitException {

        final String testDeviceId = "macAddress";
        final String testMacAddress = "macAddress";
        final String testDeviceName = "deviceName";
        final int testMeasurementCount = 3;
        final int testStepCount = 3;

        UncalibratedBeacon testBeacon = new UncalibratedBeacon(testDeviceId, testMacAddress, testDeviceName, testMeasurementCount, testStepCount);

        Queue<CalibrateBeaconViewModel.CalibrationState> stateQueue = new ArrayDeque<>();
        Queue<Integer> progressQueue = new ArrayDeque<>();
        Queue<Integer> stepQueue = new ArrayDeque<>();
        Queue<String> errorMessageQueue = new ArrayDeque<>();

        calibrateBeaconViewModel.getCurrentState().observeForever(stateQueue::add);
        calibrateBeaconViewModel.getCurrentProgress().observeForever(progressQueue::add);
        calibrateBeaconViewModel.getCurrentStep().observeForever(stepQueue::add);
        calibrateBeaconViewModel.getLatestErrorMessage().observeForever(errorMessageQueue::add);

        PublishSubject<List<BleScanResult>> publisher = PublishSubject.create();
        List<BleScanResult> results = new ArrayList<>();
        results.add(new BleScanResult(testMacAddress, -42, testDeviceName));

        int emitCounts = 0;

        when(mockBleManager.getScanningObservable(any())).thenReturn(publisher);

        new Thread() {
            @Override
            public void start() {
                calibrateBeaconViewModel.calibrate(testBeacon);
            }
        }.start();

        while (calibrateBeaconViewModel.getCurrentState().getValue() != CalibrateBeaconViewModel.CalibrationState.READY_FOR_NEXT) {
            publisher.onNext(results);
            emitCounts++;
        }

        //Emit count
        Assert.assertEquals(testMeasurementCount, emitCounts);

        //Poll first LiveData emits
        Assert.assertEquals(CalibrateBeaconViewModel.CalibrationState.IDLE, stateQueue.poll());
        Assert.assertEquals(0, progressQueue.poll().byteValue());
        Assert.assertEquals(0, stepQueue.poll().byteValue());
        Assert.assertEquals("", errorMessageQueue.poll());

        //State changes
        Assert.assertEquals(CalibrateBeaconViewModel.CalibrationState.CALIBRATION, stateQueue.poll());
        Assert.assertEquals(CalibrateBeaconViewModel.CalibrationState.READY_FOR_NEXT, stateQueue.poll());
        Assert.assertTrue(stateQueue.isEmpty());

        //Progress changes
        Assert.assertEquals(0, progressQueue.poll().byteValue());
        Assert.assertEquals(1, progressQueue.poll().byteValue());
        Assert.assertEquals(2, progressQueue.poll().byteValue());
        Assert.assertEquals(3, progressQueue.poll().byteValue());
        Assert.assertTrue(progressQueue.isEmpty());

        //Step changes
        Assert.assertEquals(1, stepQueue.poll().byteValue());
        Assert.assertTrue(stepQueue.isEmpty());

        //ErrorMessage changes
        Assert.assertEquals("", errorMessageQueue.poll());
        Assert.assertTrue(errorMessageQueue.isEmpty());

        verify(mockEvaluator, times(1)).insertRawDataSet(any(RawDataSet.class));
        verify(mockEvaluator, times(0)).evaluateAndFinish(testBeacon);
        verify(mockBleManager, times(1)).getScanningObservable(any(ArrayList.class));
        verify(mockBleManager, times(1)).stopScanning();

        verifyNoMoreInteractions(mockBleManager);
        verifyNoMoreInteractions(mockEvaluator);
    }

    @Test
    public void calibrate_SoloStep_GoThrough_WithNotChangingStep2() throws ScanFilterInvalidException {
        final int measurementCount = 3;

        UncalibratedBeacon testBeacon = new UncalibratedBeacon(
                "deviceId", "00:0A:95:9D:68:16",
                "deviceName", measurementCount, 6);

        PublishSubject<List<BleScanResult>> publisher = PublishSubject.create();
        List<BleScanResult> results = new ArrayList<>();

        when(mockBleManager.getScanningObservable(any())).thenReturn(publisher);

        calibrateBeaconViewModel.calibrate(testBeacon);
    }
}
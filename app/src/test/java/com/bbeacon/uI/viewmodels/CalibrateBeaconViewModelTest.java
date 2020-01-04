package com.bbeacon.uI.viewmodels;

import android.bluetooth.le.ScanFilter;
import android.net.MacAddress;

import com.bbeacon.backend.EvaluatorType;
import com.bbeacon.managers.BleManagerType;
import com.bbeacon.models.UncalibratedBeacon;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import static org.mockito.Mockito.mock;

class CalibrateBeaconViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantRule = new InstantTaskExecutorRule();

    private CalibrateBeaconViewModel calibrateBeaconViewModel;
    private BleManagerType mockBleManager;
    private EvaluatorType mockEvaluator;

    @BeforeEach
    void setUp() {
        mockBleManager = mock(BleManagerType.class);
        mockEvaluator = mock(EvaluatorType.class);
        calibrateBeaconViewModel = new CalibrateBeaconViewModel(mockBleManager, mockEvaluator);

    }

    @AfterEach
    void tearDown() {
        mockBleManager = null;
        mockEvaluator = null;
        calibrateBeaconViewModel = null;
    }

    @Test
    void calibrate() throws InterruptedException {
        UncalibratedBeacon testBeacon = new UncalibratedBeacon(
                "DeviceName",
                "00:02:02:34:72:a5",
                "DeviceName",
                42,
                42);

        MacAddress macAddress = MacAddress.fromString(testBeacon.getMacAddress());
        System.out.println(macAddress);


        ScanFilter scanFilter = new ScanFilter.Builder().setDeviceAddress("01:02:03:AB:CD:EF").build();
//        ScanFilter scanFilter = new ScanFilter.Builder().setDeviceAddress(testBeacon.getMacAddress()).build();

//
//        Observable<List<ScanResult>> testObservable = Observable.create(emitter -> doNothing());
//
//        when(mockBleManager.getScanningObservable(any(ArrayList.class))).thenReturn(testObservable);
//
//        calibrateBeaconViewModel.getCurrentProgress().getValue();
//        calibrateBeaconViewModel.getCurrentState().getValue();
//
//        calibrateBeaconViewModel.calibrate(testBeacon);
//
//        Assert.assertEquals(calibrateBeaconViewModel.getCurrentState().getValue(), CalibrateBeaconViewModel.CalibrationState.CALIBRATION);
    }

    @Test
    void stopScanning() {
    }

    @Test
    void quitError() {
    }
}
package com.bbeacon.managers;

import com.bbeacon.exceptions.ScanFilterInvalidException;
import com.bbeacon.models.BleScanResult;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BleManagerTest {
    private BluetoothFinder mockFinder;
    private BleManager bleManager;


    @BeforeEach
    void setUp() {
        mockFinder = mock(BluetoothFinder.class);
        bleManager = new BleManager(mockFinder);
    }

    @AfterEach
    void tearDown() {
        mockFinder = null;
        bleManager = null;
    }

    @Test
    void getScanningObservable_TestCallbackFunction() throws ScanFilterInvalidException {
        ArrayList<String> filters = new ArrayList<>();
        filters.add("testString");

        ArrayList<BleScanResult> scanResults = new ArrayList<>();
        BleScanResult testBleScanResult = new BleScanResult("macAddress", -55, "deviceName");
        scanResults.add(testBleScanResult);

        ArgumentCaptor<BleManager.BluetoothCallback> callbackArgumentCaptor = ArgumentCaptor.forClass(BleManager.BluetoothCallback.class);

        bleManager.getScanningObservable(filters).doOnNext(bleScanResults -> {
            Assert.assertEquals(scanResults, bleScanResults);
        });

        verify(mockFinder, times(1)).StartFinding(any(ArrayList.class), callbackArgumentCaptor.capture());

        BleManager.BluetoothCallback callback = callbackArgumentCaptor.getValue();
        callback.onNewScanResults(scanResults);
    }

    @Test
    void stopScanning() {
        bleManager.stopScanning();

        verify(mockFinder, times(1)).stopScanning();
    }
}
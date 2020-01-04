package com.bbeacon.managers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BleManagerTest {
    private BluetoothAdapter mockBluetoothAdapter;
    private BleManager bleManager;

    private BluetoothLeScanner mockLeScanner;

    @BeforeEach
    void setUp() {
        mockBluetoothAdapter = mock(BluetoothAdapter.class);
        bleManager = new BleManager(mockBluetoothAdapter);
        mockLeScanner = mock(BluetoothLeScanner.class);

        when(mockBluetoothAdapter.getBluetoothLeScanner()).thenReturn(mockLeScanner);
    }

    @AfterEach
    void tearDown() {
        mockBluetoothAdapter = null;
        bleManager = null;
    }

    @Test
    void getScanningObservable_NotNull() {

        ArrayList<ScanFilter> filters = new ArrayList<>();
        ArrayList<ScanResult> expectedScanResults = new ArrayList<>();

        Assert.assertNotNull(bleManager.getScanningObservable(filters));
    }

    @Test
    void stopScanning() {
        bleManager.stopScanning();
    }
}
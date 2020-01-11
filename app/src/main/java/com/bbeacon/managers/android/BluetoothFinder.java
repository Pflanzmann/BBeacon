package com.bbeacon.managers.android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Handler;
import android.util.Log;

import com.bbeacon.exceptions.ScanFilterInvalidException;
import com.bbeacon.managers.BleManager;
import com.bbeacon.models.BleScanResult;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BluetoothFinder implements BluetoothFinderType {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner scanner;
    private ScanCallback scanCallback;

    @Inject
    public BluetoothFinder(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;

        scanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    @Override
    public void StartFinding(ArrayList<String> macAddressFilter, BleManager.BluetoothCallback bleCallback) throws ScanFilterInvalidException {

        List<ScanFilter> filters = new ArrayList<>();
        for (String address : macAddressFilter) {
            try {
                filters.add(new ScanFilter.Builder().setDeviceAddress(address).build());
            } catch (Exception e) {
                throw new ScanFilterInvalidException("String is not a valid Mac-Address* " + address);
            }
        }

        scanCallback = new ScanCallback() {
            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                if (results.size() != 0) {
                    List<BleScanResult> bleScanResults = new ArrayList<>();

                    for (ScanResult scanResult : results) {
                        bleScanResults.add(new BleScanResult(
                                scanResult.getDevice().getAddress(),
                                scanResult.getRssi(),
                                scanResult.getDevice().getName(),
                                scanResult.getTxPower()));
                    }
                    bleCallback.onNewScanResults(bleScanResults);
                }
            }
        };

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanner.stopScan(scanCallback);
                    scanner.flushPendingScanResults(scanCallback);
                    ScanSettings settings = new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                            .setReportDelay(1l)
                            .build();

                    Log.d("OwnLog", "BleScan Start ");
                    scanner.flushPendingScanResults(scanCallback);
                    scanner.startScan(filters, settings, scanCallback);
                }
            }, 0);
            try {
            } catch (Exception e) {
                Log.d("OwnLog", "leScan failure: \n" + e);
            }
        }
    }

    @Override
    public void stopScanning() {
        scanner.stopScan(scanCallback);
    }
}

package com.bbeacon.managers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class BleManager implements BleManagerType {

    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner scanner;

    ScanCallback scanCallback;

    @Inject
    public BleManager(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;

        scanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    public Observable<List<ScanResult>> getScanningObservable(ArrayList<ScanFilter> filters) {
        return Observable.create(emitter -> {

            scanCallback = new ScanCallback() {
                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    if (results.size() != 0) {
                        emitter.onNext(results);
                    }
                }

                @Override
                public void onScanFailed(int errorCode) {
                    Log.d("OwnLog", "BLE scanner failed");
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

                        Log.d("OwnLog", "The reportDelay is: " + settings.getReportDelayMillis());
                        scanner.flushPendingScanResults(scanCallback);
                        scanner.startScan(filters, settings, scanCallback);

                    }
                }, 0);
                try {
                } catch (Exception e) {
                    Log.d("OwnLog", "leScan failure: \n" + e);
                }
            }
            Log.d("OwnLog", "BLE scanner done");
        });
    }

    @Override
    public void stopScanning() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            scanner.stopScan(scanCallback);
        }
    }
}

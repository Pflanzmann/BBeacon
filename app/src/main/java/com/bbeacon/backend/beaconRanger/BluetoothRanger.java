package com.bbeacon.backend.beaconRanger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

public class BluetoothRanger implements Ranger {

    BluetoothAdapter bluetoothAdapter;

    BluetoothLeScanner leScanner;

    public BluetoothRanger(BluetoothAdapter bluetoothAdapter, BluetoothLeScanner leScanner) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.leScanner = leScanner;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void startScanning(final ArrayList<ScanFilter> filters, final ScanCallback scanCallback) {

        Log.d("OwnLog", "Start App");

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {
                    leScanner.stopScan(scanCallback);

                    ScanSettings settings = new ScanSettings.Builder()
                            .setScanMode(ScanSettings.CALLBACK_TYPE_FIRST_MATCH)
                            .setReportDelay(1l)
                            .build();

                    Log.d("OwnLog", "The setting are " + settings.getReportDelayMillis());
                    leScanner.startScan(filters, settings, scanCallback);
                }
            }, 0);
            try {
                leScanner.startScan(scanCallback);
            } catch (Exception e) {
                Log.d("OwnLog", "error");
            }
        }
        Log.d("OwnLog", "Ende");
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void stopScanning(ScanCallback scanCallback) {
        leScanner.stopScan(scanCallback);
    }
}

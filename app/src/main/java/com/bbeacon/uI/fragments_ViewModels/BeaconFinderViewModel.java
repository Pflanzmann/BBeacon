package com.bbeacon.uI.fragments_ViewModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.bbeacon.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BeaconFinderViewModel extends ViewModel {

    private MutableLiveData<String> currentRSSI;

    public MutableLiveData<String> getCurrentRSSI() {
        if (currentRSSI == null) {
            currentRSSI = new MutableLiveData<String>();
        }
        return currentRSSI;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void findBluetoothDevices() {

        Log.d("OwnLog", "Start App");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        final BluetoothLeScanner leScanner = bluetoothAdapter.getBluetoothLeScanner();
        final ScanFilter filter = new ScanFilter.Builder().build();

        ArrayList<ScanFilter> filters = new ArrayList<>();
        filters.add(filter);

        final ScanCallback leScanCallback =
                new ScanCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onBatchScanResults(List<ScanResult> results) {
                        super.onBatchScanResults(results);
                        Log.d("OwnLog", "We found some devices");
                        for (ScanResult result : results) {

                            //if(result.getDevice().getName() != null)
//                            currentRSSI.postValue("deviceMac: " + result.getDevice().getAddress()
//                                    + "\nName: " + result.getDevice().getName()
//                                    + "\nRSSI: " + result.getRssi()
//                            );

                            Log.d("OwnLog", "deviceMac: " + result.getDevice().getAddress()
                                    + "\tName: " + result.getDevice().getName()
                                    + "\tRSSI: " + result.getRssi()
                            );
                        }
                    }

                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);

                        Log.d("OwnLog", "Found Some: " + result.getDevice().getName());
                    }
                };

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {
                    leScanner.stopScan(leScanCallback);

                    ScanSettings settings = new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                            .setReportDelay(1l)
                            .build();

                    Log.d("OwnLog", "The setting are " + settings.getReportDelayMillis());
                    leScanner.startScan(Collections.singletonList(filter), settings, leScanCallback);
                }
            }, 0);
            try {
                leScanner.startScan(leScanCallback);
            } catch (Exception e) {
                Log.d("OwnLog", "error");
            }
        }
        Log.d("OwnLog", "Ende");
    }


}

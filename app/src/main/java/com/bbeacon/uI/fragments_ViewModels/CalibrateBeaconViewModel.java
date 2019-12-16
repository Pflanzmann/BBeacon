package com.bbeacon.uI.fragments_ViewModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;

import com.bbeacon.backend.beaconRanger.BluetoothFinder;
import com.bbeacon.backend.beaconRanger.Ranger;
import com.bbeacon.models.TaskSuccessfulCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalibrateBeaconViewModel extends ViewModel {

    private MutableLiveData<Integer> currentStep;
    private Ranger ranger;

    public CalibrateBeaconViewModel() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner leScanner = bluetoothAdapter.getBluetoothLeScanner();

        ranger = new BluetoothFinder(bluetoothAdapter, leScanner);
    }

    public void calibrate(final String macAddress, int currentDistance, TaskSuccessfulCallback finishedCallback ) {

        ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add(new ScanFilter.Builder().setDeviceAddress(macAddress).build());

        ranger.startScanning(filters, new ScanCallback() {

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                for (ScanResult result : results) {
                    if (result.getDevice().getAddress().equals(macAddress))
                        ;
                    return;
                }
            }
        });

        finishedCallback.onTaskFinished();
    }



}

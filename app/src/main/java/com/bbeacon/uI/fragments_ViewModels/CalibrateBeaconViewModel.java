package com.bbeacon.uI.fragments_ViewModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import com.bbeacon.backend.beaconRanger.BluetoothFinder;
import com.bbeacon.backend.beaconRanger.Ranger;
import com.bbeacon.models.TaskSuccessfulCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalibrateBeaconViewModel extends ViewModel {

    private MutableLiveData<Integer> currentStep;
    private Ranger ranger;
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            getScanResults(results);
        }
    };

    public CalibrateBeaconViewModel() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner leScanner = bluetoothAdapter.getBluetoothLeScanner();

        ranger = new BluetoothFinder(bluetoothAdapter, leScanner);
    }

    public void calibrate(final String macAddress, int currentDistance, TaskSuccessfulCallback finishedCallback) {
        Log.d("OwnLog", "calibration started");


        ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add(new ScanFilter.Builder().setDeviceAddress(macAddress).build());
        ranger.startScanning(filters, scanCallback);





        Log.d("OwnLog", "calibration ended");
        finishedCallback.onTaskFinished();
    }

    private void getScanResults(List<ScanResult> results) {

    }

}

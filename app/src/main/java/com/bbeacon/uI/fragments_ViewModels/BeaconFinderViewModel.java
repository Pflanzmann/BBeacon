package com.bbeacon.uI.fragments_ViewModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.util.Log;

import com.bbeacon.backend.beaconRanger.BluetoothFinder;
import com.bbeacon.backend.beaconRanger.Ranger;
import com.bbeacon.models.UnknownBeacon;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BeaconFinderViewModel extends ViewModel {

    private MutableLiveData<String> currentRSSI;
    private MutableLiveData<ArrayList<UnknownBeacon>> foundBLEDevices;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BeaconFinderViewModel() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner leScanner = bluetoothAdapter.getBluetoothLeScanner();

        ranger = new BluetoothFinder(bluetoothAdapter, leScanner);
    }

    public MutableLiveData<String> getCurrentRSSI() {
        if (currentRSSI == null) {
            currentRSSI = new MutableLiveData<String>();
        }
        return currentRSSI;
    }

    public MutableLiveData<ArrayList<UnknownBeacon>> getFoundBLEDevices() {
        if (foundBLEDevices == null) {
            foundBLEDevices = new MutableLiveData<ArrayList<UnknownBeacon>>();
            foundBLEDevices.setValue(new ArrayList<UnknownBeacon>());
        }
        return foundBLEDevices;
    }

    Ranger ranger;
    ScanCallback leScanCallback;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void findBluetoothDevices() {

        leScanCallback = new ScanCallback() {
            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                for (ScanResult result : results) {
                    Log.d("OwnLog", "deviceMac: " + result.getDevice().getAddress()
                            + "\tName: " + result.getDevice().getName()
                            + "\tRSSI: " + result.getRssi()
                    );
                }
                addToList(results);
            }

            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                Log.d("OwnLog", "SOLO RESULT!");
            }
        };

        ranger.startScanning(new ArrayList<ScanFilter>(0), leScanCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addToList(List<ScanResult> results) {

        UnknownBeacon unknownBeacon;
        ArrayList<UnknownBeacon> beacons = getFoundBLEDevices().getValue();

        for (ScanResult result : results) {
            String address = result.getDevice().getAddress();
            String name = result.getDevice().getName();

            if (address == null)
                address = "";
            if (name == null)
                name = "";

            unknownBeacon = new UnknownBeacon(address, name);

            if (beacons == null)
                return;
            if (beacons.contains(unknownBeacon)) {
                return;
            }
            beacons.add(unknownBeacon);
            getFoundBLEDevices().postValue(beacons);
        }
    }

}

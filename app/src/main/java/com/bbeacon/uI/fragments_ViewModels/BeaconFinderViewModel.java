package com.bbeacon.uI.fragments_ViewModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import com.bbeacon.backend.beaconRanger.BluetoothFinder;
import com.bbeacon.backend.beaconRanger.Ranger;
import com.bbeacon.models.UnknownBeacon;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BeaconFinderViewModel extends ViewModel {

    private MutableLiveData<ArrayList<UnknownBeacon>> foundBLEDevices;

    Ranger ranger;

    public BeaconFinderViewModel() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner leScanner = bluetoothAdapter.getBluetoothLeScanner();

        ranger = new BluetoothFinder(bluetoothAdapter, leScanner);
    }

    public LiveData<ArrayList<UnknownBeacon>> getFoundBLEDevices() {
        if (foundBLEDevices == null) {
            foundBLEDevices = new MutableLiveData<ArrayList<UnknownBeacon>>();
            foundBLEDevices.setValue(new ArrayList<UnknownBeacon>());
        }
        return foundBLEDevices;
    }

    public void findBluetoothDevices() {
        ranger.startScanning(new ArrayList<ScanFilter>(0), leScanCallback);
    }

    ScanCallback leScanCallback = new ScanCallback() {
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
    };

    private void addToList(List<ScanResult> results) {

        UnknownBeacon unknownBeacon;
        ArrayList<UnknownBeacon> beacons = foundBLEDevices.getValue();

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
            foundBLEDevices.postValue(beacons);
        }
    }

}

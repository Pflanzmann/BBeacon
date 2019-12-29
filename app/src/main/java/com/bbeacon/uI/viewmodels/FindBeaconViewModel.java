package com.bbeacon.uI.viewmodels;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;

import com.bbeacon.managers.BleManagerType;
import com.bbeacon.models.UnknownBeacon;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FindBeaconViewModel extends ViewModel {

    private MutableLiveData<ArrayList<UnknownBeacon>> foundBLEDevices = new MutableLiveData<>(new ArrayList<UnknownBeacon>());

    BleManagerType scanner;

    @Inject
    public FindBeaconViewModel(BleManagerType bleManagerType) {
        scanner = bleManagerType;
    }

    public LiveData<ArrayList<UnknownBeacon>> getFoundBLEDevices() {
        return foundBLEDevices;
    }

    public void findBluetoothDevices() {
        scanner.getScanningObservable(new ArrayList<ScanFilter>()).subscribe(scanResults -> addToList(scanResults));
    }

    public void stopBluetoothScan() {
        scanner.stopScanning();
    }

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

    @Override
    protected void onCleared() {
        super.onCleared();

        scanner.stopScanning();
    }
}
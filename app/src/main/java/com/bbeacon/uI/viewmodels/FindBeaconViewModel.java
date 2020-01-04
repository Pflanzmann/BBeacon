package com.bbeacon.uI.viewmodels;

import android.util.Log;

import com.bbeacon.exceptions.ScanFilterInvalidException;
import com.bbeacon.managers.BleManagerType;
import com.bbeacon.models.BleScanResult;
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
        try {
            scanner.getScanningObservable(new ArrayList<>()).subscribe(scanResults -> addToList(scanResults));
        } catch (ScanFilterInvalidException e) {
            //TODO: DO SOMETHING ABOUT THE ERROR
        }
    }

    public void stopBluetoothScan() {
        scanner.stopScanning();
    }

    private void addToList(List<BleScanResult> results) {

        UnknownBeacon unknownBeacon;
        ArrayList<UnknownBeacon> beacons = foundBLEDevices.getValue();

        for (BleScanResult result : results) {
            String address = result.getMacAddress();
            String name = result.getDeviceName();
            String rssi = String.valueOf(result.getRssi());

            if (address == null)
                address = "";
            if (name == null)
                name = "";
            if (rssi == null)
                rssi = "-";

            unknownBeacon = new UnknownBeacon(address, name, rssi);

            if (beacons.contains(unknownBeacon)) {
                int index = beacons.indexOf(unknownBeacon);
                beacons.get(index).setRssi(rssi);
            } else
                beacons.add(unknownBeacon);
            foundBLEDevices.postValue(beacons);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        Log.d("OwnLog", "onCleared: stopScanning");
        scanner.stopScanning();
    }
}

package com.bbeacon.uI.fragments_ViewModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;

import com.bbeacon.managers.BluetoothManager;
import com.bbeacon.managers.BluetoothManagerType;
import com.bbeacon.models.UnknownBeacon;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BeaconFinderViewModel extends ViewModel {

    private MutableLiveData<ArrayList<UnknownBeacon>> foundBLEDevices;

    BluetoothManagerType ranger;

    public BeaconFinderViewModel() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner leScanner = bluetoothAdapter.getBluetoothLeScanner();

        ranger = new BluetoothManager(bluetoothAdapter);
    }

    public LiveData<ArrayList<UnknownBeacon>> getFoundBLEDevices() {
        if (foundBLEDevices == null) {
            foundBLEDevices = new MutableLiveData<>();
            foundBLEDevices.setValue(new ArrayList<UnknownBeacon>());
        }
        return foundBLEDevices;
    }

    public void findBluetoothDevices() {
        ranger.getScanningObservable(new ArrayList<ScanFilter>()).subscribe(scanResults -> addToList(scanResults)) ;
    }

    public void stopBluetoothScan(){
        ranger.stopScanning();
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

        ranger.stopScanning();
    }
}

package com.bbeacon.managers;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface BluetoothManagerType {

    Observable<List<ScanResult>> getScanningObservable(ArrayList<ScanFilter> filters);

    void stopScanning();
}

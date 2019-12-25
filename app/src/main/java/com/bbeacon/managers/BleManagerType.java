package com.bbeacon.managers;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface BleManagerType {

    Observable<List<ScanResult>> getScanningObservable(ArrayList<ScanFilter> filters);

    void stopScanning();
}

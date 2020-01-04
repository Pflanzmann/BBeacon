package com.bbeacon.managers;

import com.bbeacon.exceptions.ScanFilterInvalidException;
import com.bbeacon.models.BleScanResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface BleManagerType {

    Observable<List<BleScanResult>> getScanningObservable(ArrayList<String> macAddressFilter) throws ScanFilterInvalidException;

    void stopScanning();
}

package com.bbeacon.managers;

import com.bbeacon.exceptions.ScanFilterInvalidException;
import com.bbeacon.models.BleScanResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

/**
 * Provides the coupling for the BluetoothFinder
 */
public interface BleManagerType {

    /**
     * Starts the BluetoothScan and provides the observale to interact with them
     *
     * @param macAddressFilter A list of strings as MacAddress filter
     * @return A Observable list of BleScanResult
     * @throws ScanFilterInvalidException Throws if one or more Strings from the macAddressFilter are invalid MacAddresses
     */
    Observable<List<BleScanResult>> getScanningObservable(ArrayList<String> macAddressFilter) throws ScanFilterInvalidException;

    /**
     * Stops the latest Scan
     */
    void stopScanning();
}

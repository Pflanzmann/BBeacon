package com.bbeacon.managers.android;

import com.bbeacon.exceptions.ScanFilterInvalidException;
import com.bbeacon.managers.BleManager;

import java.util.ArrayList;

/**
 * Starts the Bluetoothscan on an Android device and takes a callback
 */
public interface BluetoothFinderType {
    /**
     * @param macAddressFilter A list of strings as MacAddress filter
     * @param bleCallback      The callback to handle the ScanResults
     * @throws ScanFilterInvalidException Throws if one or more Strings from the macAddressFilter are invalid MacAddresses
     */
    void StartFinding(ArrayList<String> macAddressFilter, BleManager.BluetoothCallback bleCallback) throws ScanFilterInvalidException;

    /**
     * Stops the latest Scan
     */
    void stopScanning();
}

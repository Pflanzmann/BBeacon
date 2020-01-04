package com.bbeacon.managers;

import com.bbeacon.exceptions.ScanFilterInvalidException;

import java.util.ArrayList;

public interface BluetoothFinderType {

    void StartFinding(ArrayList<String> macAddressFilter, BleManager.BluetoothCallback bleCallback) throws ScanFilterInvalidException;

    void stopScanning();
}

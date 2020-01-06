package com.bbeacon.managers.android;

import com.bbeacon.exceptions.ScanFilterInvalidException;
import com.bbeacon.managers.BleManager;

import java.util.ArrayList;

public interface BluetoothFinderType {

    void StartFinding(ArrayList<String> macAddressFilter, BleManager.BluetoothCallback bleCallback) throws ScanFilterInvalidException;

    void stopScanning();
}

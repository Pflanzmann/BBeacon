package com.bbeacon.backend.beaconRanger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;

public interface Ranger {

    public void startScanning(ScanCallback scanCallback);
    public void stopScanning(ScanCallback scanCallback);
}

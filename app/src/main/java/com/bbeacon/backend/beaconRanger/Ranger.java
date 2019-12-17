package com.bbeacon.backend.beaconRanger;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;

import java.util.ArrayList;

public interface Ranger {

    void startScanning(ArrayList<ScanFilter> filters, ScanCallback scanCallback);

    public void stopScanning(ScanCallback scanCallback);
}

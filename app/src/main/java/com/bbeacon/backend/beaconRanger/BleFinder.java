package com.bbeacon.backend.beaconRanger;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;

import java.util.ArrayList;

public class BleFinder implements Ranger {
    @Override
    public void startScanning(ArrayList<ScanFilter> filters, ScanCallback scanCallback) {

    }

    @Override
    public void stopScanning(ScanCallback scanCallback) {

    }
}

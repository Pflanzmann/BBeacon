package com.bbeacon.backend.beaconRanger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.os.Build;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

public interface Ranger {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void startScanning(ArrayList<ScanFilter> filters, ScanCallback scanCallback);

    public void stopScanning(ScanCallback scanCallback);
}

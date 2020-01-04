package com.bbeacon;

import android.bluetooth.le.ScanFilter;
import android.net.MacAddress;
import android.os.Build;

import com.bbeacon.models.UncalibratedBeacon;

import androidx.annotation.RequiresApi;

public class TestMain {

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void main(String[] args) {
        UncalibratedBeacon testBeacon = new UncalibratedBeacon(
                "DeviceName",
                "00:02:02:34:72:a5",
                "DeviceName",
                42,
                42);


        ScanFilter scanFilter = new ScanFilter.Builder().setDeviceAddress("01:02:03:AB:CD:EF").build();

        System.out.println(MacAddress.fromString("01:02:03:AB:CD:EF"));
    }

}

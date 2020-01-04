package com.bbeacon;

import android.os.Build;

import com.bbeacon.backend.EvaluatorType;
import com.bbeacon.managers.BleManagerType;
import com.bbeacon.models.UncalibratedBeacon;
import com.bbeacon.uI.viewmodels.CalibrateBeaconViewModel;

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

        CalibrateBeaconViewModel calibrateBeaconViewModel;
        BleManagerType mockBleManager = null;
        EvaluatorType mockEvaluator = null;

        calibrateBeaconViewModel = new CalibrateBeaconViewModel(mockBleManager, mockEvaluator);


        calibrateBeaconViewModel.getCurrentState().observe(null, calibrationState -> {
        });

    }

}

package com.bbeacon.uI.fragments_ViewModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import com.bbeacon.backend.BeaconCalibrationEvaluator;
import com.bbeacon.backend.beaconRanger.BluetoothFinder;
import com.bbeacon.backend.beaconRanger.Ranger;
import com.bbeacon.models.RawDataSet;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalibrateBeaconViewModel extends ViewModel {

    private final int STEP_COUNT = 5;
    private final int MAX_FAILURE_MEASSUREMENTS = 5;

    public LiveData<CalibrationState> getCurrentState() {
        return currentState;
    }

    public LiveData<Integer> getCurrentProgress() {
        return currentProgress;
    }

    private MutableLiveData<CalibrationState> currentState = new MutableLiveData<>(CalibrationState.IDLE);
    private MutableLiveData<Integer> currentProgress = new MutableLiveData<>(0);

    private int currentStep;
    private Ranger ranger;
    private BeaconCalibrationEvaluator evaluator;

    private ArrayList<Integer> tempMeasurements = new ArrayList<>();
    private int meassureFailures = 5;

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            Log.d("OwnLog", "Solo ScanResult");
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.d("OwnLog", "Scan FAILED!!!!");
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            if (results.size() == 1 && tempMeasurements.size() < STEP_COUNT) {
                Log.d("OwnLog", "Scan Result: " + results.get(0).getRssi());

                tempMeasurements.add(results.get(0).getRssi());
            } else {
                Log.d("OwnLog", "Result failure: " + meassureFailures);

                meassureFailures--;
                if (meassureFailures == MAX_FAILURE_MEASSUREMENTS) {
                    currentState.postValue(CalibrationState.ERROR);
                }
            }
            maxMessurements();
        }
    };

    public CalibrateBeaconViewModel() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner leScanner = bluetoothAdapter.getBluetoothLeScanner();

        ranger = new BluetoothFinder(bluetoothAdapter, leScanner);
        evaluator = new BeaconCalibrationEvaluator();
    }

    public void calibrate(final String macAddress, int currentDistance) {
        if (currentState.getValue() != CalibrationState.IDLE)
            return;

        Log.d("OwnLog", "calibration started");

        currentState.postValue(CalibrationState.CALIBRATION);
        currentStep = currentDistance;

        final ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add(new ScanFilter.Builder().setDeviceAddress(macAddress).build());

        ranger.startScanning(filters, scanCallback);

        Log.d("OwnLog", "calibration method done");
    }

    private void maxMessurements() {
        if (tempMeasurements.size() != STEP_COUNT)
            return;

        evaluator.insertRawDataSet(new RawDataSet<>(currentStep, tempMeasurements.toArray(new Integer[5])));

        ranger.stopScanning(scanCallback);
        currentState.postValue(CalibrationState.DONE);
    }

    private void quitError(){
        tempMeasurements = new ArrayList<>();
        meassureFailures = 0;
        ranger.stopScanning(scanCallback);

        currentState.postValue(CalibrationState.IDLE);
    }

    public enum CalibrationState {
        IDLE,
        ERROR,
        CALIBRATION,
        DONE
    }
}

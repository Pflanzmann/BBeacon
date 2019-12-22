package com.bbeacon.uI.fragments_ViewModels;

import android.bluetooth.BluetoothAdapter;
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
import java.util.Observable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.disposables.Disposable;

public class CalibrateBeaconViewModel extends ViewModel {

    private final int MESSUREMENT_COUNT = 5;

    public LiveData<CalibrationState> getCurrentState() {
        return currentState;
    }

    public LiveData<Integer> getCurrentProgress() {
        return currentProgress;
    }

    private MutableLiveData<CalibrationState> currentState = new MutableLiveData<>(CalibrationState.IDLE);
    private MutableLiveData<Integer> currentProgress = new MutableLiveData<>(0);

    private BluetoothFinder ranger;
    private BeaconCalibrationEvaluator evaluator;

    public CalibrateBeaconViewModel() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        ranger = new BluetoothFinder(bluetoothAdapter);
        evaluator = new BeaconCalibrationEvaluator();
    }

    public void calibrate(final String macAddress, int currentDistance) {
        if (currentState.getValue() != CalibrationState.IDLE)
            return;

        Log.d("OwnLog", "calibration started");
        currentState.postValue(CalibrationState.CALIBRATION);

        final ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add(new ScanFilter.Builder().setDeviceAddress(macAddress).build());

        ranger.getScanningObservable(filters)
                .doOnNext(lists -> currentProgress.postValue(currentProgress.getValue() + 1))
                .buffer(MESSUREMENT_COUNT)
                .subscribe(lists -> {
                    ranger.stopScanning();

                    ArrayList<Integer> tempMeasurements = new ArrayList<>();

                    for (List<ScanResult> results : lists)
                        for (ScanResult result : results) {
                            tempMeasurements.add(result.getRssi());
                            Log.d("OwnLog", "those are the result RSSI : " + result.getRssi());
                        }

                    evaluator.insertRawDataSet(new RawDataSet<>(currentDistance, tempMeasurements.toArray(new Integer[5])));
                    currentState.postValue(CalibrationState.DONE);
                });
        Log.d("OwnLog", "calibration method done");
    }

    public void quitErrororReset() {
        ranger.stopScanning();

        currentState.postValue(CalibrationState.IDLE);
    }

    public enum CalibrationState {
        IDLE,
        ERROR,
        CALIBRATION,
        DONE
    }
}

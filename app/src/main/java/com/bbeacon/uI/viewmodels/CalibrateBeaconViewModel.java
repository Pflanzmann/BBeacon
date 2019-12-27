package com.bbeacon.uI.viewmodels;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import com.bbeacon.backend.Evaluator;
import com.bbeacon.managers.BleManagerType;
import com.bbeacon.models.RawDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.disposables.Disposable;

public class CalibrateBeaconViewModel extends ViewModel {

    private final int MESSUREMENT_COUNT = 5;
    private final int MAX_STEPS = 1;

    private MutableLiveData<CalibrationState> currentState = new MutableLiveData<>(CalibrationState.IDLE);
    private MutableLiveData<Integer> currentProgress = new MutableLiveData<>(0);
    private MutableLiveData<Integer> currentStep = new MutableLiveData<>(0);

    private BleManagerType scanner;
    private Evaluator evaluator;

    private Disposable disposable;

    @Inject
    public CalibrateBeaconViewModel(BleManagerType scanner, Evaluator evaluator) {
        this.scanner = scanner;
        this.evaluator = evaluator;
    }

    public LiveData<CalibrationState> getCurrentState() {
        return currentState;
    }

    public LiveData<Integer> getCurrentProgress() {
        return currentProgress;
    }

    public LiveData<Integer> getCurrentStep() {
        return currentStep;
    }


    public void calibrate(String macAddress) {
        if (currentState.getValue() != CalibrationState.IDLE && currentState.getValue() != CalibrationState.READY)
            return;

        Log.d("OwnLog", "calibration started");
        currentProgress.setValue(0);
        currentState.postValue(CalibrationState.CALIBRATION);

        //Filter setup for the scanner
        final ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add(new ScanFilter.Builder().setDeviceAddress(macAddress).build());

        disposable = scanner.getScanningObservable(filters)
                .doOnNext(lists -> currentProgress.postValue(currentProgress.getValue() + 1))
                .timeout(10, TimeUnit.SECONDS, observer -> {
                    Log.d("OwnLog", "calibration disposed");
                    currentState.postValue(CalibrationState.ERROR);
                    disposable.dispose();
                })
                .buffer(MESSUREMENT_COUNT)
                .subscribe(lists -> {
                    scanner.stopScanning();

                    ArrayList<Integer> tempMeasurements = new ArrayList<>();

                    for (List<ScanResult> results : lists)
                        for (ScanResult result : results) {
                            tempMeasurements.add(result.getRssi());
                            Log.d("OwnLog", "those are the result RSSI : " + result.getRssi());
                        }

                    if (tempMeasurements.size() != MESSUREMENT_COUNT) {
                        Log.d("OwnLog", "calibration disposed");
                        currentState.postValue(CalibrationState.ERROR);
                        disposable.dispose();
                        return;
                    }

                    evaluator.insertRawDataSet(new RawDataSet<>(currentStep.getValue(), tempMeasurements.toArray(new Integer[MESSUREMENT_COUNT])));

                    if (currentStep.getValue() == MAX_STEPS) {

                        String name = lists.get(0).get(0).getDevice().getName();

                        evaluator.evaluateAndFinish(name, macAddress);
                        currentState.postValue(CalibrationState.DONE);
                    } else {
                        currentStep.postValue(currentStep.getValue() + 1);
                        currentState.postValue(CalibrationState.READY);
                        disposable.dispose();
                    }
                });
    }

    public void quitError() {
        scanner.stopScanning();
        disposable.dispose();

        currentState.postValue(CalibrationState.IDLE);
    }

    public enum CalibrationState {
        IDLE,
        ERROR,
        CALIBRATION,
        READY,
        DONE
    }
}

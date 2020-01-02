package com.bbeacon.uI.viewmodels;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import com.bbeacon.backend.Evaluator;
import com.bbeacon.exceptions.DataSetDoesNotFitException;
import com.bbeacon.managers.BleManagerType;
import com.bbeacon.models.RawDataSet;
import com.bbeacon.models.UncalibratedBeacon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.disposables.Disposable;

public class CalibrateBeaconViewModel extends ViewModel {

    private MutableLiveData<CalibrationState> currentState = new MutableLiveData<>(CalibrationState.IDLE);
    private MutableLiveData<Integer> currentProgress = new MutableLiveData<>(0);
    private MutableLiveData<Integer> currentStep = new MutableLiveData<>(0);

    private MutableLiveData<String> latestError = new MutableLiveData<>("");

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

    public LiveData<String> getLatestError() {
        return latestError;
    }

    public void calibrate(UncalibratedBeacon uncalibratedBeacon) {
        if (currentState.getValue() != CalibrationState.IDLE && currentState.getValue() != CalibrationState.READY)
            return;

        Log.d("OwnLog", "calibration started");
        currentProgress.setValue(0);
        latestError.postValue("");
        currentState.postValue(CalibrationState.CALIBRATION);

        //Filter setup for the scanner
        final ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();

        filters.add(new ScanFilter.Builder().setDeviceAddress(uncalibratedBeacon.getMacAddress()).build());

        disposable = scanner.getScanningObservable(filters)
                .doOnNext(lists -> currentProgress.postValue(currentProgress.getValue() + 1))
                .timeout(15, TimeUnit.SECONDS, observer -> {
                    Log.d("OwnLog", "Timeout: calibration disposed");
                    latestError.postValue("Timeout - could not find the beacon.");
                    currentState.postValue(CalibrationState.ERROR);
                    disposable.dispose();
                })
                .buffer(uncalibratedBeacon.getMeasurementCount())
                .subscribe(lists -> {
                    scanner.stopScanning();

                    ArrayList<Integer> tempMeasurements = new ArrayList<>();

                    for (List<ScanResult> results : lists)
                        for (ScanResult result : results)
                            tempMeasurements.add(result.getRssi());


                    if (tempMeasurements.size() != uncalibratedBeacon.getMeasurementCount()) {
                        Log.d("OwnLog", "calibration disposed");
                        currentState.postValue(CalibrationState.ERROR);
                        disposable.dispose();
                        return;
                    }

                    try {
                        evaluator.insertRawDataSet(new RawDataSet<Integer>(currentStep.getValue(), tempMeasurements.toArray(new Integer[uncalibratedBeacon.getMeasurementCount()])));
                    } catch (DataSetDoesNotFitException e) {
                        Log.d("OwnLog", "Invalid DataSet");
                        latestError.postValue("DataSet is invalid and does not fit.");
                        currentState.postValue(CalibrationState.ERROR);
                        disposable.dispose();
                        return;
                    }

                    if (currentStep.getValue() == uncalibratedBeacon.getCalibrationSteps() -1) {
                        evaluator.evaluateAndFinish(uncalibratedBeacon);
                        currentState.postValue(CalibrationState.DONE);
                    } else {
                        currentStep.postValue(currentStep.getValue() + 1);
                        currentState.postValue(CalibrationState.READY);
                        disposable.dispose();
                    }
                });
    }

    public void stopScanning() {
        scanner.stopScanning();
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

package com.bbeacon.uI.viewmodels;

import android.util.Log;

import com.bbeacon.backend.EvaluatorType;
import com.bbeacon.exceptions.DataSetDoesNotFitException;
import com.bbeacon.exceptions.ScanFilterInvalidException;
import com.bbeacon.managers.BleManagerType;
import com.bbeacon.managers.storage.BeaconStorageManagerType;
import com.bbeacon.models.BleScanResult;
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
    private MutableLiveData<String> latestErrorMessage = new MutableLiveData<>("");

    private BleManagerType bleManager;
    private EvaluatorType evaluator;
    private BeaconStorageManagerType beaconStorageManager;

    private Disposable disposable;

    @Inject
    public CalibrateBeaconViewModel(BleManagerType bleManager, EvaluatorType evaluator, BeaconStorageManagerType beaconStorageManager) {
        this.bleManager = bleManager;
        this.evaluator = evaluator;
        this.beaconStorageManager = beaconStorageManager;
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

    public LiveData<String> getLatestErrorMessage() {
        return latestErrorMessage;
    }

    int currentProgessCache = 0;

    public void calibrate(UncalibratedBeacon uncalibratedBeacon) {
        if (currentState.getValue() != CalibrationState.IDLE && currentState.getValue() != CalibrationState.READY_FOR_NEXT) {
            latestErrorMessage.postValue("Calibration is already going");
            return;
        }

        Log.d("OwnLog", "calibration started");
        currentProgessCache = 0;
        currentProgress.postValue(currentProgessCache);
        latestErrorMessage.postValue("");
        currentState.postValue(CalibrationState.CALIBRATION);

        ArrayList<String> filterString = new ArrayList<>();
        filterString.add(uncalibratedBeacon.getMacAddress());

        try {
            disposable = bleManager.getScanningObservable(filterString)
                    .doOnEach(lists -> currentProgress.postValue(++currentProgessCache))
                    .timeout(15, TimeUnit.SECONDS, observer -> {
                        Log.d("OwnLog", "Timeout: calibration disposed");
                        latestErrorMessage.postValue("Timeout - could not find the beacon.");
                        currentState.postValue(CalibrationState.ERROR);
                        disposable.dispose();
                    })
                    .buffer(uncalibratedBeacon.getMeasurementCount())
                    .subscribe(lists -> {
                        bleManager.stopScanning();

                        ArrayList<Integer> tempMeasurements = new ArrayList<>();

                        for (List<BleScanResult> results : lists) {
                            for (BleScanResult result : results) {
                                tempMeasurements.add(result.getRssi());
                            }
                        }

                        if (tempMeasurements.size() != uncalibratedBeacon.getMeasurementCount()) {
                            Log.d("OwnLog", "calibration disposed: wrong measurements-count");
                            latestErrorMessage.postValue("MeasurementCount does not fit");
                            currentState.postValue(CalibrationState.ERROR);
                            disposable.dispose();
                            return;
                        }

                        try {
                            evaluator.insertRawDataSet(new RawDataSet<Integer>(currentStep.getValue(), tempMeasurements.toArray(new Integer[uncalibratedBeacon.getMeasurementCount()])));
                        } catch (DataSetDoesNotFitException e) {
                            System.out.println("we are in the case");
                            Log.d("OwnLog", "Invalid DataSet");
                            latestErrorMessage.postValue("DataSet is Invalid");
                            currentState.postValue(CalibrationState.ERROR);
                            disposable.dispose();
                            return;
                        }

                        if (currentStep.getValue() == uncalibratedBeacon.getCalibrationSteps() - 1) {
                            beaconStorageManager.storeBeacon(evaluator.evaluateAndFinish(uncalibratedBeacon));
                            currentState.postValue(CalibrationState.DONE);
                            disposable.dispose();
                        } else {
                            currentStep.postValue(currentStep.getValue() + 1);
                            currentState.postValue(CalibrationState.READY_FOR_NEXT);
                            disposable.dispose();
                        }
                    });
        } catch (ScanFilterInvalidException e) {
            currentState.postValue(CalibrationState.ERROR);
            latestErrorMessage.postValue("MacAddress is invalid");
        }
    }

    public void stopScanning() {
        if (disposable != null)
            disposable.dispose();

        bleManager.stopScanning();
    }

    public void quitError() {
        bleManager.stopScanning();

        if (disposable != null)
            disposable.dispose();

        currentState.postValue(CalibrationState.IDLE);
    }

    public enum CalibrationState {
        IDLE,
        ERROR,
        CALIBRATION,
        READY_FOR_NEXT,
        DONE
    }
}

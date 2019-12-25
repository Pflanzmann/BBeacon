package com.bbeacon.uI.fragments_ViewModels;

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
    private final int MAX_STEPS = 10;

    private MutableLiveData<CalibrationState> currentState = new MutableLiveData<>(CalibrationState.IDLE);
    private MutableLiveData<Integer> currentProgress = new MutableLiveData<>(0);
    private MutableLiveData<Integer> currentStep = new MutableLiveData<>(0);

    private BleManagerType ranger;
    private Evaluator evaluator;

    private Disposable disposable;

    @Inject
    public CalibrateBeaconViewModel(BleManagerType ranger, Evaluator evaluator) {
        this.ranger = ranger;
        this.evaluator = evaluator;

        Log.d("OwnLog", "calib-ranger: " + ranger.hashCode());
        Log.d("OwnLog", "calib-evaluator: " + evaluator.hashCode());
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


    public void calibrate(final String macAddress) {
        if (currentState.getValue() != CalibrationState.IDLE)
            return;

        Log.d("OwnLog", "calibration started");
        currentState.postValue(CalibrationState.CALIBRATION);

        final ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add(new ScanFilter.Builder().setDeviceAddress(macAddress).build());

        disposable = ranger.getScanningObservable(filters)
                .doOnNext(lists -> currentProgress.postValue(currentProgress.getValue() + 1))
                .timeout(10, TimeUnit.SECONDS, observer -> {
                    Log.d("OwnLog", "calibration disposed");
                    currentState.postValue(CalibrationState.ERROR);
                    disposable.dispose();
                })
                .buffer(MESSUREMENT_COUNT)
                .subscribe(lists -> {
                    ranger.stopScanning();

                    ArrayList<Integer> tempMeasurements = new ArrayList<>();

                    for (List<ScanResult> results : lists)
                        for (ScanResult result : results) {
                            tempMeasurements.add(result.getRssi());
                            Log.d("OwnLog", "those are the result RSSI : " + result.getRssi());
                        }

                    evaluator.insertRawDataSet(new RawDataSet<>(currentStep.getValue(), tempMeasurements.toArray(new Integer[MESSUREMENT_COUNT])));

                    if (currentStep.getValue() == MAX_STEPS) {
                        evaluator.printAll();
                        return;
                    }

                    currentStep.postValue(currentStep.getValue() + 1);
                    currentState.postValue(CalibrationState.DONE);
                    disposable.dispose();
                });
        Log.d("OwnLog", "calibration method done");
    }

    public void quitErrorReset() {
        ranger.stopScanning();
        currentProgress.setValue(0);
        disposable.dispose();

        currentState.postValue(CalibrationState.IDLE);
    }


    public enum CalibrationState {
        IDLE,
        ERROR,
        CALIBRATION,
        DONE
    }
}

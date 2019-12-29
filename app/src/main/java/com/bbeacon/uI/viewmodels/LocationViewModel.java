package com.bbeacon.uI.viewmodels;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;

import com.bbeacon.backend.AverageRanger;
import com.bbeacon.backend.RangerType;
import com.bbeacon.managers.BleManagerType;
import com.bbeacon.managers.RoomManagerType;
import com.bbeacon.models.CalibratedBeacon;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.disposables.Disposable;

public class LocationViewModel extends ViewModel {

    MutableLiveData<String> currentDistance = new MutableLiveData<String>("0");

    private BleManagerType bleManager;
    private RoomManagerType roomManager;

    private Disposable disposable;

    private RangerType ranger;

    @Inject
    public LocationViewModel(BleManagerType bleManager, RoomManagerType roomManager) {
        this.bleManager = bleManager;
        this.roomManager = roomManager;
    }

    public LiveData<String> getCurrentDistance() {
        return currentDistance;
    }

    public void getRanges() {
        CalibratedBeacon beacon = roomManager.getBeaconByPositionIndexOrNull(0);

        ranger = new AverageRanger(beacon);

        final ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add(new ScanFilter.Builder().setDeviceAddress(beacon.getMacAddress()).build());


        disposable = bleManager.getScanningObservable(filters)
                .subscribe(lists -> onNewResult(lists));
    }

    private void onNewResult(List<ScanResult> results) {
        int rssi = results.get(0).getRssi();

        currentDistance.postValue(String.valueOf("Distance: " + ranger.computeDistance(rssi) + " RSSI: " + rssi));
    }
}

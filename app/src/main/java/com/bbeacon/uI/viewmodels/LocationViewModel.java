package com.bbeacon.uI.viewmodels;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import com.bbeacon.backend.AverageRanger;
import com.bbeacon.backend.CalculatorType;
import com.bbeacon.backend.RangerType;
import com.bbeacon.exceptions.CouldNotFindBeaconByIdException;
import com.bbeacon.managers.BleManagerType;
import com.bbeacon.managers.RoomManagerType;
import com.bbeacon.models.CalibratedBeacon;
import com.bbeacon.models.PositionedBeacon;
import com.bbeacon.models.RangedBeaconPosition;
import com.bbeacon.models.UserPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocationViewModel extends ViewModel {

    private MutableLiveData<UserPosition> currentUserPosition = new MutableLiveData<>(new UserPosition(0, 0));

    private MutableLiveData<String> currentTest = new MutableLiveData<String>("none");

    private BleManagerType bleManager;
    private RoomManagerType roomManager;
    private CalculatorType calculator;
    private Map<String, RangerType> rangers = new HashMap<String, RangerType>();

    @Inject
    public LocationViewModel(BleManagerType bleManager, RoomManagerType roomManager, CalculatorType calculator) {
        this.bleManager = bleManager;
        this.roomManager = roomManager;
        this.calculator = calculator;
    }

    public LiveData<String> getCurrentTest() {
        return currentTest;
    }

    public LiveData<UserPosition> getCurrentUserPosition() {
        return currentUserPosition;
    }

    public void startLocating() {
        final ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();

        PositionedBeacon[] positionedBeacon = roomManager.getRoom().getBeaconPositions();

        Log.d("OwnLog", "startLocating: test : " + positionedBeacon[0].getX());

        for (int i = 0; i < positionedBeacon.length; i++) {

            if (positionedBeacon[i] == null)
                break;

            rangers.put(roomManager.getRoom().getBeaconPositions()[i].getMacAddress(), new AverageRanger(positionedBeacon[i]));

            filters.add(new ScanFilter.Builder().setDeviceAddress(positionedBeacon[i].getMacAddress()).build());
        }

        bleManager.getScanningObservable(filters)
                .subscribe(lists -> onNewResult(lists));

    }

    private void onNewResult(List<ScanResult> results) {
        if (results.size() < 3)
            return;

        List<RangedBeaconPosition> rangedBeaconPositions = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {

            RangerType ranger = rangers.get(results.get(i).getDevice().getAddress());

            try {
                PositionedBeacon position = roomManager.getBeaconByIndex(i);
                rangedBeaconPositions.add(new RangedBeaconPosition(position, ranger.computeDistance(results.get(i).getRssi())));

            } catch (CouldNotFindBeaconByIdException e) {
                e.printStackTrace();
            }

            if (rangedBeaconPositions.size() == 3)
                break;
        }

        UserPosition userPosition = calculator.getCoordinate(rangedBeaconPositions);

        currentTest.postValue("X: " + userPosition.getX() + " Y: " + userPosition.getY());
        currentUserPosition.postValue(userPosition);
    }

    public void stopLocating() {
        bleManager.stopScanning();
    }

    public void startTestRanging() {
        final ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();

        try {
            CalibratedBeacon beacon = roomManager.getBeaconByIndex(0);

            rangers.put(beacon.getMacAddress(), new AverageRanger(beacon));

            filters.add(new ScanFilter.Builder().setDeviceAddress(beacon.getMacAddress()).build());

        } catch (CouldNotFindBeaconByIdException e) {
            e.printStackTrace();
        }
        bleManager.getScanningObservable(filters)
                .subscribe(lists -> onNewResultTest(lists));
    }

    private void onNewResultTest(List<ScanResult> results) {
        int rssi = results.get(0).getRssi();

        RangerType ranger = rangers.get(results.get(0).getDevice().getAddress());

        if (ranger != null)
            currentTest.postValue("Distance: " + ranger.computeDistance(rssi) + " RSSI: " + rssi);
    }
}

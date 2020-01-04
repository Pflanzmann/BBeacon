package com.bbeacon.uI.viewmodels;

import com.bbeacon.backend.AverageRanger;
import com.bbeacon.backend.CalculatorType;
import com.bbeacon.backend.RangerType;
import com.bbeacon.exceptions.ScanFilterInvalidException;
import com.bbeacon.managers.BleManagerType;
import com.bbeacon.managers.RoomManagerType;
import com.bbeacon.models.BleScanResult;
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
        final ArrayList<String> filters = new ArrayList<>();

        PositionedBeacon[] positionedBeacon = roomManager.getRoom().getBeaconPositions();

        for (int i = 0; i < positionedBeacon.length; i++) {

            if (positionedBeacon[i] == null)
                break;

            rangers.put(roomManager.getRoom().getBeaconPositions()[i].getMacAddress(), new AverageRanger(positionedBeacon[i]));

            filters.add(positionedBeacon[i].getMacAddress());
        }

        try {
            bleManager.getScanningObservable(filters)
                    .subscribe(lists -> onNewResult(lists));
        } catch (ScanFilterInvalidException e) {
            //TODO: DO SOMETHING ABOUT THE ERROR
        }

    }

    private void onNewResult(List<BleScanResult> results) {
        if (results.size() < 3)
            return;

        List<RangedBeaconPosition> rangedBeaconPositions = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {

            RangerType ranger = rangers.get(results.get(i).getMacAddress());

            try {
                PositionedBeacon position = roomManager.getBeaconByIndex(i);
                rangedBeaconPositions.add(new RangedBeaconPosition(position, ranger.computeDistance(results.get(i).getRssi())));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (rangedBeaconPositions.size() == 3)
                break;
        }

        if (rangedBeaconPositions.size() != 3)
            return;

        UserPosition userPosition = calculator.getCoordinate(rangedBeaconPositions);

        currentTest.postValue("X: " + userPosition.getX() + " Y: " + userPosition.getY());
        currentUserPosition.postValue(userPosition);
    }

    public void stopLocating() {
        bleManager.stopScanning();
    }

    public void startTestRanging() {
        final ArrayList<String> filters = new ArrayList<>();

        try {
            CalibratedBeacon beacon = roomManager.getBeaconByIndex(0);

            rangers.put(beacon.getMacAddress(), new AverageRanger(beacon));

            filters.add(beacon.getMacAddress());

            bleManager.getScanningObservable(filters)
                    .subscribe(lists -> onNewResultTest(lists));

        } catch (Exception e) {
            //TODO: DO SOMETHING ABOUT THE ERROR´s
            e.printStackTrace();
        }
    }

    private void onNewResultTest(List<BleScanResult> results) {
        int rssi = results.get(0).getRssi();

        RangerType ranger = rangers.get(results.get(0).getMacAddress());

        if (ranger != null)
            currentTest.postValue("Distance: " + ranger.computeDistance(rssi) + " RSSI: " + rssi);
    }
}

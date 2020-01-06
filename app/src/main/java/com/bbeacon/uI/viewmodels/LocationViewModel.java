package com.bbeacon.uI.viewmodels;

import com.bbeacon.backend.AverageRanger;
import com.bbeacon.backend.CalculatorType;
import com.bbeacon.backend.RangerType;
import com.bbeacon.backend.TxPowerRanger;
import com.bbeacon.exceptions.CouldNotFindBeaconByIdException;
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

    private MutableLiveData<String> currentTest1 = new MutableLiveData<String>("none");
    private MutableLiveData<String> currentTest2 = new MutableLiveData<String>("none");
    private MutableLiveData<String> currentTest3 = new MutableLiveData<String>("none");

    public LiveData<String> getCurrentTest2() {
        return currentTest2;
    }

    public LiveData<String> getCurrentTest3() {
        return currentTest3;
    }

    public LiveData<String> getCurrentTest4() {
        return currentTest4;
    }

    private MutableLiveData<String> currentTest4 = new MutableLiveData<String>("none");

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

    public LiveData<String> getCurrentTest1() {
        return currentTest1;
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

        currentTest1.postValue("X: " + userPosition.getX() + " Y: " + userPosition.getY());
        currentUserPosition.postValue(userPosition);
    }

    public void stopLocating() {
        bleManager.stopScanning();
    }

    public void startTestRanging() {
        final ArrayList<String> filters = new ArrayList<>();

        try {
            CalibratedBeacon beacon = roomManager.getBeaconByIndex(0);

            rangers.put(beacon.getMacAddress(), new TxPowerRanger(beacon));

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
            currentTest1.postValue("Distance: " + ranger.computeDistance(rssi) + " RSSI: " + rssi);
    }

    public void startTestRangingRoom() {
        final ArrayList<String> filters = new ArrayList<>();

        try {
            CalibratedBeacon beacon1 = roomManager.getBeaconByIndex(0);
            CalibratedBeacon beacon2 = roomManager.getBeaconByIndex(1);
            CalibratedBeacon beacon3 = roomManager.getBeaconByIndex(2);
            CalibratedBeacon beacon4 = roomManager.getBeaconByIndex(3);

            rangers.put(beacon1.getMacAddress(), new TxPowerRanger(beacon1));
            rangers.put(beacon2.getMacAddress(), new TxPowerRanger(beacon2));
            rangers.put(beacon3.getMacAddress(), new TxPowerRanger(beacon3));
            rangers.put(beacon4.getMacAddress(), new TxPowerRanger(beacon4));

            filters.add(beacon1.getMacAddress());
            filters.add(beacon2.getMacAddress());
            filters.add(beacon3.getMacAddress());
            filters.add(beacon4.getMacAddress());

            bleManager.getScanningObservable(filters)
                    .subscribe(lists -> onNewResultTestRoom(lists));

        } catch (Exception e) {
            //TODO: DO SOMETHING ABOUT THE ERROR´s
            e.printStackTrace();
        }
    }

    private void onNewResultTestRoom(List<BleScanResult> results) throws CouldNotFindBeaconByIdException {
        for (BleScanResult result : results) {
            String output = "";

            RangerType ranger = rangers.get(result.getMacAddress());

            int rssi = result.getRssi();

            double distance = ranger.computeDistance(rssi);
            distance = Math.round(distance * 100.0) / 100.0;

            if (ranger != null) {
                output += ranger.getBeacon().getDeviceId() + "->\t\tDistance: " + distance + "\t\tRSSI: " + rssi;
            }

            if (ranger.getBeacon().getMacAddress() == roomManager.getBeaconByIndex(0).getMacAddress())
                currentTest1.postValue(output);
            else if (ranger.getBeacon().getMacAddress() == roomManager.getBeaconByIndex(1).getMacAddress())
                currentTest2.postValue(output);
            else if (ranger.getBeacon().getMacAddress() == roomManager.getBeaconByIndex(2).getMacAddress())
                currentTest3.postValue(output);
            else if (ranger.getBeacon().getMacAddress() == roomManager.getBeaconByIndex(3).getMacAddress())
                currentTest4.postValue(output);
        }
    }
}

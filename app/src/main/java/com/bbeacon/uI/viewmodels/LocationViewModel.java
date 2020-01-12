package com.bbeacon.uI.viewmodels;

import com.bbeacon.backend.CalculatorType;
import com.bbeacon.backend.RangerType;
import com.bbeacon.backend.TxPowerRanger;
import com.bbeacon.exceptions.ScanFilterInvalidException;
import com.bbeacon.managers.BleManagerType;
import com.bbeacon.managers.RoomManagerType;
import com.bbeacon.models.BleScanResult;
import com.bbeacon.models.PositionedBeacon;
import com.bbeacon.models.RangedBeaconPosition;
import com.bbeacon.models.UserPosition;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocationViewModel extends ViewModel {

    private MutableLiveData<String> currentTest1 = new MutableLiveData<String>("none");
    private MutableLiveData<String> currentTest2 = new MutableLiveData<String>("none");

    public LiveData<String> getCurrentTest2() {
        return currentTest2;
    }
    public LiveData<String> getCurrentTest1() {
        return currentTest1;
    }

    private BleManagerType bleManager;
    private RoomManagerType roomManager;
    private CalculatorType calculator;

    private Map<String, RangerType> rangers = new HashMap<String, RangerType>();
    private Queue<UserPosition> latestPositions = new ArrayDeque<>();

    private final int averageSize = 5;

    @Inject
    public LocationViewModel(BleManagerType bleManager, RoomManagerType roomManager, CalculatorType calculator) {
        this.bleManager = bleManager;
        this.roomManager = roomManager;
        this.calculator = calculator;
    }

    public void startLocating() {
        final ArrayList<String> filters = new ArrayList<>();

        PositionedBeacon[] positionedBeacon = roomManager.getRoom().getBeaconPositions();

        for (int i = 0; i < positionedBeacon.length; i++) {

            if (positionedBeacon[i] == null)
                continue;

            rangers.put(roomManager.getRoom().getBeaconPositions()[i].getMacAddress(), new TxPowerRanger(positionedBeacon[i]));

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
        List<RangedBeaconPosition> rangedBeaconPositions = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {

            RangerType ranger = rangers.get(results.get(i).getMacAddress());

            try {
                PositionedBeacon position = roomManager.getBeaconByIndex(i);
                rangedBeaconPositions.add(new RangedBeaconPosition(position, ranger.computeDistance(results.get(i).getRssi())));
            } catch (Exception e) {
                continue;
            }
        }

        UserPosition userPosition = calculator.getCoordinate(rangedBeaconPositions);

        if (userPosition == null) {
            currentTest1.postValue("current  X: --- | Y: ---");
            return;
        }

        while (latestPositions.size() >= averageSize) {
            latestPositions.poll();
        }

        latestPositions.add(userPosition);

        UserPosition[] allPositions = new UserPosition[averageSize];
        latestPositions.toArray(allPositions);


        double averageX = 0, averageY = 0, count = 0;

        for (UserPosition user : allPositions) {
            if (user == null)
                break;

            averageX += clamp(user.getX(), -1, 4);
            averageY += clamp(user.getY(), -1, 4);

            count++;
        }

        averageX = averageX / count;
        averageY = averageY / count;

        currentTest1.postValue("current X: " + decima2(userPosition.getX()) + " Y: " + decima2(userPosition.getY()));
        currentTest2.postValue("Average X: " + decima2(averageX) + " Y: " + decima2(averageY));
    }

    public void stopLocating() {
        bleManager.stopScanning();
    }

    private double decima2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private double clamp(double value, int min, int max) {
        if (value < min)
            return min;
        else if (value > max)
            return max;
        else
            return value;
    }
}

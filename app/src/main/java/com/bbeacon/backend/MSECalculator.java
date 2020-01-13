package com.bbeacon.backend;

import android.util.Log;

import com.bbeacon.models.RangedBeaconPosition;
import com.bbeacon.models.UserPosition;

import java.util.List;

import javax.inject.Inject;

//least mean squared error
public class MSECalculator implements CalculatorType {

    @Inject
    public MSECalculator() {
    }

    @Override
    public UserPosition getCoordinate(List<RangedBeaconPosition> rangedPositions) {
        if (rangedPositions.size() < 3)
            return null;

        Log.d("OwnLog", "getCoordinate: " + rangedPositions.get(0).getRange());

        double latestBest = 1000000000;
        double temp = 0;
        double bestX = -1;
        double bestY = -1;

        for (double i = 0; i < 4.1; i += 0.2f) {
            for (double j = 0; j < 4.1; j += 0.2f) {
                temp = mse(i, j, rangedPositions);
                if (temp < latestBest && temp > 0) {
                    bestX = i;
                    bestY = j;
                    latestBest = temp;
                }
            }
        }
        return new UserPosition(bestX, bestY);
    }

    private double mse(double positionX, double positionY, List<RangedBeaconPosition> beacons) {

        float x, y;
        double temp = 0;

        for (RangedBeaconPosition beacon : beacons) {
            x = beacon.getPositionedBeacon().getX();
            y = beacon.getPositionedBeacon().getY();

            double dis = Math.sqrt((positionX - x) * (positionX - x) + (positionY - y) * (positionY - y));

            temp += ((beacon.getRange() - dis) * (beacon.getRange() - dis));
        }

        return temp;
    }
}

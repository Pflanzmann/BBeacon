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
        if (rangedPositions.size() == 0)
            return null;

        rangedPositions.sort((o1, o2) -> {
            if (o1.getRange() < o2.getRange())
                return -1;
            else
                return 1;
        });

        Log.d("OwnLog", "getCoordinate: " + rangedPositions.get(0).getRange());

        double latestBest = 1000000000;
        double temp = 0;
        double bestX = -1;
        double bestY = -1;
        float closestX = rangedPositions.get(0).getPositionedBeacon().getX();
        float closestY = rangedPositions.get(0).getPositionedBeacon().getY();


        for (double i = 0; i < 4.1; i += 0.2f) {
            for (double j = 0; j < 4.1; j += 0.2f) {
                temp = mse(i, j, rangedPositions, closestX, closestY);
                if (temp < latestBest && temp > 0) {
                    bestX = i;
                    bestY = j;
                    latestBest = temp;
                }
            }
        }

        return new UserPosition(bestX, bestY);
    }

    private double mse(double positionX, double positionY, List<RangedBeaconPosition> beacons, float closestX, float closestY) {
//        double dis2 = Math.sqrt((positionX - closestX) * (positionX - closestX) + (positionY - closestY) * (positionY - closestY));
//
//        if (dis2 > 3)
//            return -1;

        float x = 0;
        float y = 0;

        double temp = 0;
        double dis = 0;

        for (RangedBeaconPosition beacon : beacons) {
            x = beacon.getPositionedBeacon().getX();
            y = beacon.getPositionedBeacon().getY();

            dis = Math.sqrt((positionX - x) * (positionX - x) + (positionY - y) * (positionY - y));

            temp += ((beacon.getRange() - dis) * (beacon.getRange() - dis));
        }

        //range1-dist(X, beacon1)^2
        return temp;
    }
}
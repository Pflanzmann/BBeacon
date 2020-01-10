package com.bbeacon.backend;

import android.util.Log;

import com.bbeacon.models.RangedBeaconPosition;
import com.bbeacon.models.UserPosition;

import java.util.List;

import javax.inject.Inject;

public class CalculatorExample implements CalculatorType {

    @Inject
    public CalculatorExample() {
    }

    @Override
    public UserPosition getCoordinate(List<RangedBeaconPosition> rangedPositions) {
        if (rangedPositions.size() != 3)
            return null;

        double distanceA = rangedPositions.get(0).getRange();
        double distanceB = rangedPositions.get(1).getRange();
        double distanceC = rangedPositions.get(2).getRange();

        float pointA1 = rangedPositions.get(0).getPositionedBeacon().getX();
        float pointA2 = rangedPositions.get(0).getPositionedBeacon().getY();

        float pointB1 = rangedPositions.get(1).getPositionedBeacon().getX();
        float pointB2 = rangedPositions.get(1).getPositionedBeacon().getY();

        float pointC1 = rangedPositions.get(2).getPositionedBeacon().getX();
        float pointC2 = rangedPositions.get(2).getPositionedBeacon().getY();

        double w, z, x, y, y2;


        w = distanceA * distanceA - distanceB * distanceB - pointA1 * pointA1 - pointA2 * pointA2 + pointB1 * pointB1 + pointB2 * pointB2;

        z = distanceB * distanceB - distanceC * distanceC - pointB1 * pointB1 - pointB2 * pointB2 + pointC1 * pointC1 + pointC2 * pointC2;

        x = (w * (pointC2 - pointB2) - z * (pointB2 - pointA2)) / (2 * ((pointB1 - pointA1) * (pointC1 - pointB2) - (pointC1 - pointB1) * (pointB2 - pointA2)));

        y = (w - 2 * x * (pointB1 - pointA1)) / (2 * (pointB2 - pointA2));
        Log.d("OwnLog", "getCoordinate: Y: " + y);

        y2 = (z - 2 * x * (pointC1 - pointB1)) / (2 * (pointC1 - pointB2));

        y = (y + y2) / 2;

        Log.d("OwnLog", "getCoordinate: Y2: " + y2);
        Log.d("OwnLog", "getCoordinate: Y3: " + y);

        return new UserPosition(y, y2);
    }
}

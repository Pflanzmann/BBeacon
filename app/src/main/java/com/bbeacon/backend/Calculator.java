package com.bbeacon.backend;

import com.bbeacon.models.RangedBeaconPosition;
import com.bbeacon.models.UserPosition;

import java.util.List;

import javax.inject.Inject;

public class Calculator implements CalculatorType {

    @Inject
    public Calculator() {
    }

    @Override
    public UserPosition getCoordinate(List<RangedBeaconPosition> rangedPositions) {
        if (rangedPositions.size() <= 2)
            return null;

        double x1 = rangedPositions.get(0).getPositionedBeacon().getX();
        double y1 = rangedPositions.get(0).getPositionedBeacon().getY();
        double range1 = rangedPositions.get(0).getRange();

        double x2 = rangedPositions.get(1).getPositionedBeacon().getX();
        double y2 = rangedPositions.get(1).getPositionedBeacon().getY();
        double range2 = rangedPositions.get(1).getRange();

        double x3 = rangedPositions.get(2).getPositionedBeacon().getX();
        double y3 = rangedPositions.get(2).getPositionedBeacon().getY();
        double range3 = rangedPositions.get(2).getRange();

        double a = 2 * x2 - 2 * x1;
        double b = 2 * y2 - 2 * y1;
        double c = Math.pow(range1, 2) - Math.pow(range2, 2) - Math.pow(x1, 2) + Math.pow(x2, 2) - Math.pow(y1, 2) + Math.pow(y2, 2);
        double d = 2 * x3 - 2 * x2;
        double e = 2 * y3 - 2 * y2;
        double f = Math.pow(range2, 2) - Math.pow(range3, 2) - Math.pow(x2, 2) + Math.pow(x3, 2) - Math.pow(y2, 2) + Math.pow(y3, 2);
        double x = ((c * e - f * b) / (e * a - b * d));
        double y = ((c * d - a * f) / (b * d - a * e));

        return new UserPosition(x, y);
    }
}

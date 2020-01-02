package com.bbeacon.backend;

import com.bbeacon.models.RangedBeaconPosition;
import com.bbeacon.models.UserPosition;

import java.util.List;

public interface CalculatorType {

    UserPosition getCoordinate(List<RangedBeaconPosition> rangedPositions);
}

package com.bbeacon.backend;

import com.bbeacon.models.RangedBeaconPosition;
import com.bbeacon.models.UserPosition;

import java.util.List;

/**
 * Calculates the UserPosition from a list of RangedBeaconsPosition
 */
public interface CalculatorType {

    /**
     * @param rangedPositions A list of every measured beacon with his calculated range and position
     * @return Estimated Position of the User or null if an error occurs
     */
    UserPosition getCoordinate(List<RangedBeaconPosition> rangedPositions);
}

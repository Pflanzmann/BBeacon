package com.bbeacon.backend;

import com.bbeacon.models.CalibratedBeacon;

/**
 * Calculates the distance from the rssi
 */
public interface RangerType {

    /**
     * @param rssi: the measured rssi from a beacon
     * @return the calculated distance in meter
     */
    double computeDistance(int rssi);

    CalibratedBeacon getBeacon();
}

package com.bbeacon.backend;

import com.bbeacon.models.CalibratedBeacon;

public interface RangerType {

    double computeDistance(int rssi);

    CalibratedBeacon getBeacon();
}

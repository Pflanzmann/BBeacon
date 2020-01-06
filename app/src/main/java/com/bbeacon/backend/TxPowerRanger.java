package com.bbeacon.backend;

import com.bbeacon.models.CalibratedBeacon;

import javax.inject.Inject;

public class TxPowerRanger implements RangerType {

    private CalibratedBeacon beacon;

    private int txPower = -59;

    @Inject
    public TxPowerRanger(CalibratedBeacon beacon) {
        this.beacon = beacon;
    }

    @Override
    public double computeDistance(int rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine distance, return -1.
        }

        double ratio = rssi * 1.0 / txPower;

        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }

    @Override
    public CalibratedBeacon getBeacon() {
        return beacon;
    }
}

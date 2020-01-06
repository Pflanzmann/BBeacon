package com.bbeacon.models;

import java.io.Serializable;

public class RangedBeaconPosition implements Serializable {
    private PositionedBeacon positionedBeacon;
    private double range;


    public RangedBeaconPosition(PositionedBeacon positionedBeacon, double range) {
        this.positionedBeacon = positionedBeacon;
        this.range = range;
    }

    public PositionedBeacon getPositionedBeacon() {
        return positionedBeacon;
    }

    public double getRange() {
        return range;
    }
}

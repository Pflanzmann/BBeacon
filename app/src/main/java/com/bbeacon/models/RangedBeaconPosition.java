package com.bbeacon.models;

import java.io.Serializable;

public class RangedBeaconPosition implements Serializable {
    public PositionedBeacon getPositionedBeacon() {
        return positionedBeacon;
    }

    public float getRange() {
        return range;
    }

    PositionedBeacon positionedBeacon;
    float range;

    public RangedBeaconPosition(PositionedBeacon positionedBeacon, float range) {
        this.positionedBeacon = positionedBeacon;
        this.range = range;
    }
}

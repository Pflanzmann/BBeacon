package com.bbeacon.models;

import java.io.Serializable;

public class Room implements Serializable {

    private PositionedBeacon[] beaconPositions;

    public Room(PositionedBeacon[] beaconPositions) {
        this.beaconPositions = beaconPositions;
    }

    public Room(int size) {
        this.beaconPositions = new PositionedBeacon[size];
    }

    public PositionedBeacon[] getBeaconPositions() {
        return beaconPositions;
    }
}

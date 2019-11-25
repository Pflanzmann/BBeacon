package com.bbeacon.models;

import java.util.List;

public class Room {

    private List<Position> beaconPositions;

    public Room(List<Position> beaconPositions) {
        this.beaconPositions = beaconPositions;
    }

    public List<Position> getBeaconPositions() {
        return beaconPositions;
    }
}

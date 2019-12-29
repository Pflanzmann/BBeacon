package com.bbeacon.models;

public class Room {

    private BeaconPosition[] beaconPositions;

    public Room(BeaconPosition[] beaconPositions) {
        this.beaconPositions = beaconPositions;
    }

    public Room(int size) {
        this.beaconPositions = new BeaconPosition[size];
    }

    public BeaconPosition[] getBeaconPositions() {
        return beaconPositions;
    }
}

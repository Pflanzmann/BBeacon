package com.bbeacon.managers;

import com.bbeacon.managers.Storage.BeaconStorageManagerType;
import com.bbeacon.managers.Storage.RoomStorageManagerType;
import com.bbeacon.models.BeaconPosition;
import com.bbeacon.models.CalibratedBeacon;
import com.bbeacon.models.Room;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RoomManager implements RoomManagerType {

    private Room room;
    private RoomStorageManagerType roomStorageManager;
    private BeaconStorageManagerType beaconStorageManager;

    @Inject
    public RoomManager(RoomStorageManagerType roomStorageManager, BeaconStorageManagerType beaconStorageManager) {
        this.roomStorageManager = roomStorageManager;
        this.beaconStorageManager = beaconStorageManager;

        room = roomStorageManager.loadRoom();
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public void setPositionOn(int index, BeaconPosition position) {
        if (room.getBeaconPositions().length <= index || index < 0)
            return;

        BeaconPosition[] positions = room.getBeaconPositions();
        positions[index] = position;
        room = new Room(positions);

        storeRoom();
    }

    @Override
    public CalibratedBeacon getBeaconByPositionIndexOrNull(int index) {
        if (room.getBeaconPositions().length <= index || index < 0)
            return null;

        BeaconPosition[] positions = room.getBeaconPositions();

        return beaconStorageManager.loadBeaconById(positions[index].getBeaconId());
    }

    private void storeRoom() {
        roomStorageManager.storeRoom(room);
    }
}

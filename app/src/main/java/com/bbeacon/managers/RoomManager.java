package com.bbeacon.managers;

import android.util.Log;

import com.bbeacon.exceptions.CouldNotFindBeaconByIdException;
import com.bbeacon.exceptions.PositionIndexOutOfBound;
import com.bbeacon.managers.Storage.BeaconStorageManagerType;
import com.bbeacon.managers.Storage.RoomStorageManagerType;
import com.bbeacon.models.PositionedBeacon;
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
    public void setPositionOn(int index, PositionedBeacon position) throws PositionIndexOutOfBound {
        if (index >= room.getBeaconPositions().length || index < 0)
            throw new PositionIndexOutOfBound();

        Log.d("OwnLog", "setPositionOn: start");

        PositionedBeacon[] positions = room.getBeaconPositions();

        for (int i = 0; i < positions.length; i++) {
            if (positions[i] != null && positions[i].getMacAddress().equals(position.getMacAddress()))
                positions[i] = null;
        }

        positions[index] = position;
        room = new Room(positions);

        storeRoom();
    }

    @Override
    public PositionedBeacon getBeaconByIndex(int index) throws CouldNotFindBeaconByIdException {
        if (room.getBeaconPositions().length <= index || index < 0)
            throw new IndexOutOfBoundsException();

        PositionedBeacon[] positions = room.getBeaconPositions();

        if (positions[index] == null)
            throw new CouldNotFindBeaconByIdException();

        return positions[index];
    }

    @Override
    public void deleteBeaconFromRoom(String deviceId) throws CouldNotFindBeaconByIdException {
        PositionedBeacon[] positions = room.getBeaconPositions();

        for (int i = 0; i < positions.length; i++) {
            if (positions[i] != null && positions[i].getDeviceId().equals(deviceId))
                positions[i] = null;
        }

        room = new Room(positions);
        storeRoom();
    }

    private void storeRoom() {
        roomStorageManager.storeRoom(room);
    }
}

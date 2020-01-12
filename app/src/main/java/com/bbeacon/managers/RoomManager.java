package com.bbeacon.managers;

import android.util.Log;

import com.bbeacon.exceptions.CouldNotFindBeaconByIndexException;
import com.bbeacon.exceptions.NoRoomFoundException;
import com.bbeacon.exceptions.PositionIndexOutOfBound;
import com.bbeacon.managers.storage.SingleRoomStorageManagerType;
import com.bbeacon.models.PositionedBeacon;
import com.bbeacon.models.Room;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RoomManager implements RoomManagerType {

    private Room room;
    private SingleRoomStorageManagerType roomStorageManager;

    @Inject
    public RoomManager(SingleRoomStorageManagerType roomStorageManager) {
        this.roomStorageManager = roomStorageManager;

        try {
            room = roomStorageManager.loadRoom();
        } catch (NoRoomFoundException e) {
            room = new Room(4);
        }
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
    public PositionedBeacon getBeaconByIndex(int index) throws CouldNotFindBeaconByIndexException {
        if (room.getBeaconPositions().length <= index || index < 0)
            throw new IndexOutOfBoundsException();

        PositionedBeacon[] positions = room.getBeaconPositions();

        if (positions[index] == null)
            throw new CouldNotFindBeaconByIndexException();

        return positions[index];
    }

    @Override
    public void removeBeaconFromRoomById(String deviceId) {
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

package com.bbeacon.managers;

import com.bbeacon.exceptions.CouldNotFindBeaconByIdException;
import com.bbeacon.exceptions.PositionIndexOutOfBound;
import com.bbeacon.models.PositionedBeacon;
import com.bbeacon.models.Room;

public interface RoomManagerType {

    Room getRoom();

    void setPositionOn(int index, PositionedBeacon position) throws PositionIndexOutOfBound;

    PositionedBeacon getBeaconByIndex(int index) throws CouldNotFindBeaconByIdException;

    void removeBeaconFromRoomById(String deviceId);
}

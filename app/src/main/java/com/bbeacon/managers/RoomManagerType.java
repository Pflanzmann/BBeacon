package com.bbeacon.managers;

import com.bbeacon.exceptions.CouldNotFindBeaconByIndexException;
import com.bbeacon.exceptions.PositionIndexOutOfBound;
import com.bbeacon.models.PositionedBeacon;
import com.bbeacon.models.Room;

/**
 * Manages all room-interactions
 */
public interface RoomManagerType {

    /**
     * Provides the room reference
     *
     * @return The room
     */
    Room getRoom();

    /**
     * Sets on the chosen position a beacon
     *
     * @param index    The index of the chosen position
     * @param position the PositionedBeacon that should be placed
     * @throws PositionIndexOutOfBound Throws if the chosen index is no existing
     */
    void setPositionOn(int index, PositionedBeacon position) throws PositionIndexOutOfBound;

    /**
     * Returns a beacon by index
     *
     * @param index    The index of the chosen position
     * @return The beacon for the chosen index
     * @throws CouldNotFindBeaconByIndexException Throws if there is no beacon on the position
     */
    PositionedBeacon getBeaconByIndex(int index) throws CouldNotFindBeaconByIndexException;

    /**
     *
     * @param deviceId Removes a beacon and sets it to 0 by his deviceId
     */
    void removeBeaconFromRoomById(String deviceId);
}

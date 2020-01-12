package com.bbeacon.managers.storage;

import com.bbeacon.exceptions.NoRoomFoundException;
import com.bbeacon.models.Room;

/**
 * Handles the Storage of one Room
 */
public interface SingleRoomStorageManagerType {

    /**
     * Adds a room to the storage
     *
     * @param room The room that should be added in the storage
     */
    void storeRoom(Room room);

    /**
     *
     * @return The room
     * @throws NoRoomFoundException Throws if there is no room found
     */
    Room loadRoom() throws NoRoomFoundException;
}

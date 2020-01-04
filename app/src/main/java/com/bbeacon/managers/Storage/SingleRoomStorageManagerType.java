package com.bbeacon.managers.Storage;

import com.bbeacon.exceptions.NoRoomFoundException;
import com.bbeacon.models.Room;

public interface SingleRoomStorageManagerType {

    void storeRoom(Room room);

    Room loadRoom() throws NoRoomFoundException;
}

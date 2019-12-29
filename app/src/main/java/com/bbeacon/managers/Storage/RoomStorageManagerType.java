package com.bbeacon.managers.Storage;

import com.bbeacon.models.Room;

public interface RoomStorageManagerType {

    void storeRoom(Room room);

    Room loadRoom();
}

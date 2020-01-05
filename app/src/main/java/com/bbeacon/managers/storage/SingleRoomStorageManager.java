package com.bbeacon.managers.storage;

import com.bbeacon.exceptions.NoRoomFoundException;
import com.bbeacon.exceptions.NothingToLoadException;
import com.bbeacon.models.Room;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SingleRoomStorageManager implements SingleRoomStorageManagerType {

    private StorageLocker storageLocker;

    @Inject
    public SingleRoomStorageManager(StorageLocker storageLocker) {
        this.storageLocker = storageLocker;
    }

    @Override
    public void storeRoom(Room room) {
        storageLocker.store(new Gson().toJson(room), StorageLocker.StorageKey.Single_ROOM);
    }

    @Override
    public Room loadRoom() throws NoRoomFoundException {

        String loadedRoomString;
        try {
            loadedRoomString = storageLocker.load(StorageLocker.StorageKey.Single_ROOM);
        } catch (NothingToLoadException e) {
            throw new NoRoomFoundException();
        }

        Gson gson = new Gson();

        Room room = gson.fromJson(loadedRoomString, Room.class);
        return room;
    }
}

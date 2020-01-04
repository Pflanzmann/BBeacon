package com.bbeacon.managers.Storage;

import com.bbeacon.exceptions.NoRoomFoundException;
import com.bbeacon.exceptions.NothingToLoadException;
import com.bbeacon.models.Room;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class SingleRoomStorageLockerTest {

    private StorageLocker mockStorageLocker;
    private SingleRoomStorageManager roomStorageManager;

    @BeforeEach
    void setUp() {
        mockStorageLocker = mock(StorageLocker.class);
        roomStorageManager = new SingleRoomStorageManager(mockStorageLocker);
    }

    @AfterEach
    void tearDown() {
        mockStorageLocker = null;
        roomStorageManager = null;
    }

    @Test
    void storeRoom_Success() throws NothingToLoadException {
        Random random = new Random();

        Room room = new Room(random.nextInt(10000));
        String roomJsonString = new Gson().toJson(room);

        roomStorageManager.storeRoom(room);

        verify(mockStorageLocker, times(1)).store(roomJsonString, StorageLocker.StorageKey.Single_ROOM);
        verifyNoMoreInteractions(mockStorageLocker);
    }

    @Test
    void loadRoom_Success() throws NothingToLoadException, NoRoomFoundException {
        Random random = new Random();

        Room room = new Room(random.nextInt(9999999));
        String roomJsonString = new Gson().toJson(room);

        when(mockStorageLocker.load(StorageLocker.StorageKey.Single_ROOM)).thenReturn(roomJsonString);

        Room returnedRoom = roomStorageManager.loadRoom();

        Assert.assertTrue(room.getBeaconPositions().length == returnedRoom.getBeaconPositions().length);
        verify(mockStorageLocker, times(1)).load(StorageLocker.StorageKey.Single_ROOM);
        verifyNoMoreInteractions(mockStorageLocker);
    }

    @Test
    void loadAllBeacons_() throws NothingToLoadException, NoRoomFoundException {
        Random random = new Random();

        Room room = new Room(random.nextInt(10000));
        String roomJsonString = new Gson().toJson(room);

        when(mockStorageLocker.load(StorageLocker.StorageKey.Single_ROOM)).thenThrow(NothingToLoadException.class);

        Assertions.assertThrows(NoRoomFoundException.class, () -> {
            Room returnedRoom = roomStorageManager.loadRoom();
        });

        verify(mockStorageLocker, times(1)).load(StorageLocker.StorageKey.Single_ROOM);
        verifyNoMoreInteractions(mockStorageLocker);
    }
}

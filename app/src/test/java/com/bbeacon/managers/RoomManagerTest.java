package com.bbeacon.managers;

import com.bbeacon.exceptions.CouldNotFindBeaconByIndexException;
import com.bbeacon.exceptions.NoRoomFoundException;
import com.bbeacon.exceptions.PositionIndexOutOfBound;
import com.bbeacon.managers.storage.SingleRoomStorageManager;
import com.bbeacon.models.PositionedBeacon;
import com.bbeacon.models.Room;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RoomManagerTest {

    private final int STANDART_BEACON_SIZE = 4;

    private SingleRoomStorageManager mockSingleRoomStorageManager;
    private RoomManager roomManager;

    @BeforeEach
    void setUp() {
        mockSingleRoomStorageManager = mock(SingleRoomStorageManager.class);
    }

    @AfterEach
    void tearDown() {
        mockSingleRoomStorageManager = null;
        roomManager = null;
    }

    @Test
    void getRoom_Success() throws NoRoomFoundException {
        Room testRoom = new Room(new Random().nextInt(9999999));

        when(mockSingleRoomStorageManager.loadRoom()).thenReturn(testRoom);
        roomManager = new RoomManager(mockSingleRoomStorageManager);
        Room returnedRoom = roomManager.getRoom();

        Assert.assertEquals(testRoom, returnedRoom);
    }

    @Test
    void getRoom_InitializeWithNewRoom_Through_NoRoomFoundException() throws NoRoomFoundException {
        when(mockSingleRoomStorageManager.loadRoom()).thenThrow(NoRoomFoundException.class);
        roomManager = new RoomManager(mockSingleRoomStorageManager);
        PositionedBeacon[] returnedBeaconList = roomManager.getRoom().getBeaconPositions();

        boolean isEmpty = true;

        for (int i = 0; i < returnedBeaconList.length; i++) {
            if (returnedBeaconList[i] != null)
                isEmpty = false;
        }
        Assert.assertTrue(isEmpty);
        Assert.assertEquals(STANDART_BEACON_SIZE, returnedBeaconList.length);
    }


    @Test
    void setPositionOn_Success_RandomRoomSize_RandomIndex() throws NoRoomFoundException, PositionIndexOutOfBound {
        final int testRoomSize = new Random().nextInt(999);
        final int testRoomPosition = new Random().nextInt(testRoomSize);

        PositionedBeacon mockBeacon1 = mock(PositionedBeacon.class);
        Room testRoom = new Room(testRoomSize);
        when(mockSingleRoomStorageManager.loadRoom()).thenReturn(testRoom);
        roomManager = new RoomManager(mockSingleRoomStorageManager);

        roomManager.setPositionOn(testRoomPosition, mockBeacon1);

        ArgumentCaptor<Room> valueCapture = ArgumentCaptor.forClass(Room.class);

        verify(mockSingleRoomStorageManager, times(1)).storeRoom(valueCapture.capture());

        Room roomResult = valueCapture.getValue();

        Assert.assertEquals(testRoomSize, roomResult.getBeaconPositions().length);
    }

    @Test
    void setPositionOn_PositionIndexOutOfBound_TooLarge() throws NoRoomFoundException {
        final int testRoomSize = new Random().nextInt(999);
        final int testRoomPosition = testRoomSize + 1;

        PositionedBeacon mockBeacon = mock(PositionedBeacon.class);
        Room testRoom = new Room(testRoomSize);
        when(mockSingleRoomStorageManager.loadRoom()).thenReturn(testRoom);
        roomManager = new RoomManager(mockSingleRoomStorageManager);

        Assertions.assertThrows(PositionIndexOutOfBound.class, () -> {
            roomManager.setPositionOn(testRoomPosition, mockBeacon);
        });
    }

    @Test
    void setPositionOn_PositionIndexOutOfBound_NegativeNumber() throws NoRoomFoundException {
        final int testRoomSize = new Random().nextInt(999);
        final int testRoomPosition = -1;

        PositionedBeacon mockBeacon = mock(PositionedBeacon.class);

        Room testRoom = new Room(testRoomSize);
        when(mockSingleRoomStorageManager.loadRoom()).thenReturn(testRoom);
        roomManager = new RoomManager(mockSingleRoomStorageManager);

        Assertions.assertThrows(PositionIndexOutOfBound.class, () -> {
            roomManager.setPositionOn(testRoomPosition, mockBeacon);
        });
    }

    @Test
    void getBeaconByIndex_Success() throws NoRoomFoundException, CouldNotFindBeaconByIndexException {
        final int testRoomSize = new Random().nextInt(999);
        final int testRoomPosition = new Random().nextInt(testRoomSize);

        PositionedBeacon mockBeacon = mock(PositionedBeacon.class);
        PositionedBeacon[] positionedBeacons = new PositionedBeacon[testRoomSize];
        positionedBeacons[testRoomPosition] = mockBeacon;

        Room testRoom = new Room(positionedBeacons);
        when(mockSingleRoomStorageManager.loadRoom()).thenReturn(testRoom);
        roomManager = new RoomManager(mockSingleRoomStorageManager);

        PositionedBeacon returnedBeacon = roomManager.getBeaconByIndex(testRoomPosition);

        Assert.assertEquals(mockBeacon, returnedBeacon);
    }

    @Test
    void getBeaconByIndex_CouldNotFindBeaconByIdException() throws NoRoomFoundException, CouldNotFindBeaconByIndexException {
        final int testRoomSize = new Random().nextInt(999);
        final int testRoomPosition = new Random().nextInt(testRoomSize);

        PositionedBeacon mockBeacon = mock(PositionedBeacon.class);
        PositionedBeacon[] positionedBeacons = new PositionedBeacon[testRoomSize];

        Room testRoom = new Room(positionedBeacons);
        when(mockSingleRoomStorageManager.loadRoom()).thenReturn(testRoom);
        roomManager = new RoomManager(mockSingleRoomStorageManager);

        Assertions.assertThrows(CouldNotFindBeaconByIndexException.class, () -> {
            roomManager.getBeaconByIndex(testRoomPosition);
        });
    }

    @Test
    void removeBeaconFromRoom() throws NoRoomFoundException {
        final int testRoomSize = new Random().nextInt(999);
        final int testRoomPosition = new Random().nextInt(testRoomSize);
        final String testDeviceId = "DeviceId";

        PositionedBeacon mockBeacon = mock(PositionedBeacon.class);
        PositionedBeacon[] positionedBeacons = new PositionedBeacon[testRoomSize];
        positionedBeacons[testRoomPosition] = mockBeacon;

        Room testRoom = new Room(positionedBeacons);
        when(mockSingleRoomStorageManager.loadRoom()).thenReturn(testRoom);
        when(mockBeacon.getDeviceId()).thenReturn(testDeviceId);
        roomManager = new RoomManager(mockSingleRoomStorageManager);

        ArgumentCaptor<Room> valueCapture = ArgumentCaptor.forClass(Room.class);

        roomManager.removeBeaconFromRoomById(testDeviceId);
        verify(mockSingleRoomStorageManager, times(1)).storeRoom(valueCapture.capture());

        Room roomResult = valueCapture.getValue();
        PositionedBeacon[] returnedBeaconList = roomResult.getBeaconPositions();

        boolean containsBeacon = false;
        int containedBeaconCount = 0;
        for (int i = 0; i < returnedBeaconList.length; i++) {
            if (returnedBeaconList[i] != null) {
                containedBeaconCount++;

                if (returnedBeaconList[i].equals(mockBeacon))
                    containsBeacon = true;
            }
        }

        Assert.assertFalse(containsBeacon);
        Assert.assertEquals(0, containedBeaconCount);
        Assert.assertEquals(testRoomSize, roomResult.getBeaconPositions().length);
    }
}
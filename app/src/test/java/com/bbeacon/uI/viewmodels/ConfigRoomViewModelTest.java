package com.bbeacon.uI.viewmodels;

import com.bbeacon.exceptions.PositionIndexOutOfBound;
import com.bbeacon.managers.RoomManager;
import com.bbeacon.models.CalibratedBeacon;
import com.bbeacon.models.PositionedBeacon;
import com.bbeacon.models.Room;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Random;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ConfigRoomViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private Room testRoom;
    private RoomManager mockRoomManager;
    private ConfigRoomViewModel configRoomViewModel;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        mockRoomManager = null;
        configRoomViewModel = null;
    }

    @Test
    void getCurrentRoom() {
        mockRoomManager = mock(RoomManager.class);
        testRoom = new Room(4);

        when(mockRoomManager.getRoom()).thenReturn(testRoom);
        configRoomViewModel = new ConfigRoomViewModel(mockRoomManager);

        Room roomResult = configRoomViewModel.getCurrentRoom().getValue();

        Assert.assertEquals(testRoom, roomResult);
    }

    @Test
    void setBeaconOn() throws PositionIndexOutOfBound {
        final int testIndex = new Random().nextInt(3);

        mockRoomManager = mock(RoomManager.class);
        testRoom = new Room(4);

        when(mockRoomManager.getRoom()).thenReturn(testRoom);
        configRoomViewModel = new ConfigRoomViewModel(mockRoomManager);

        CalibratedBeacon testBeacon = mock(CalibratedBeacon.class);
        ArgumentCaptor<PositionedBeacon> argumentCaptor = ArgumentCaptor.forClass(PositionedBeacon.class);

        configRoomViewModel.setBeaconOn(testIndex, testBeacon);

        verify(mockRoomManager, times(1)).setPositionOn(eq(testIndex), argumentCaptor.capture());

        PositionedBeacon resultBeacon = argumentCaptor.getValue();

        int expectedX = 0;
        int expectedY = 0;
        switch (testIndex) {
            case 0:
                expectedX = 0;
                expectedY = 0;
                break;

            case 1:
                expectedX = 4;
                expectedY = 0;
                break;

            case 2:
                expectedX = 0;
                expectedY = 4;
                break;

            case 3:
                expectedX = 4;
                expectedY = 4;
                break;
        }

        Assert.assertEquals(expectedX, resultBeacon.getX(), 0);
        Assert.assertEquals(expectedY, resultBeacon.getY(), 0);

        verify(mockRoomManager, times(2)).getRoom();
        verifyNoMoreInteractions(mockRoomManager);
    }

    @Test
    void setBeaconOn_WrongIndex() throws PositionIndexOutOfBound {
        final int testIndexNegative = -1;
        final int testIndexTooHigh = 4;

        mockRoomManager = mock(RoomManager.class);
        testRoom = new Room(4);

        when(mockRoomManager.getRoom()).thenReturn(testRoom);
        configRoomViewModel = new ConfigRoomViewModel(mockRoomManager);

        CalibratedBeacon testBeacon = mock(CalibratedBeacon.class);
        ArgumentCaptor<PositionedBeacon> argumentCaptor = ArgumentCaptor.forClass(PositionedBeacon.class);

        configRoomViewModel.setBeaconOn(testIndexNegative, testBeacon);
        configRoomViewModel.setBeaconOn(testIndexTooHigh, testBeacon);

        verify(mockRoomManager, times(0)).setPositionOn(eq(testIndexNegative), argumentCaptor.capture());
        verify(mockRoomManager, times(0)).setPositionOn(eq(testIndexTooHigh), argumentCaptor.capture());
        verify(mockRoomManager, times(1)).getRoom();
        verifyNoMoreInteractions(mockRoomManager);
    }

}
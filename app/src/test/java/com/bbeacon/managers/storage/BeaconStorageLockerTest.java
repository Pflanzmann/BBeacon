package com.bbeacon.managers.storage;

import com.bbeacon.exceptions.CouldNotFindBeaconByIdException;
import com.bbeacon.exceptions.NothingToLoadException;
import com.bbeacon.models.CalibratedBeacon;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class BeaconStorageManagerTest {

    private StorageLocker mockStorageLocker;
    private BeaconStorageManager beaconStorageManager;

    @BeforeEach
    void setUp() {
        mockStorageLocker = mock(StorageLocker.class);
        beaconStorageManager = new BeaconStorageManager(mockStorageLocker);
    }

    @AfterEach
    void tearDown() {
        mockStorageLocker = null;
        beaconStorageManager = null;
    }

    @Test
    void storeBeacon_Success() throws NothingToLoadException {
        CalibratedBeacon beacon = new CalibratedBeacon(
                "deviceId",
                "macAddress",
                "deviceName",
                42,
                42,
                new Date(30),
                new ArrayList<>());

        HashMap<String, CalibratedBeacon> beaconMap = new HashMap<>();
        beaconMap.put(beacon.getDeviceId(), beacon);

        String beaconMapString = new Gson().toJson(beaconMap);

        when(mockStorageLocker.load(StorageLockerType.StorageKey.BEACON)).thenReturn("[]");

        beaconStorageManager.storeBeacon(beacon);

        verify(mockStorageLocker, times(1)).load(StorageLockerType.StorageKey.BEACON);
        verify(mockStorageLocker, times(1)).store(beaconMapString, StorageLockerType.StorageKey.BEACON);
        verifyNoMoreInteractions(mockStorageLocker);
    }

    @Test
    void loadAllBeacons_GetOneBeacon() throws NothingToLoadException {
        CalibratedBeacon beacon = new CalibratedBeacon(
                "deviceId",
                "macAddress",
                "deviceName",
                42,
                42,
                new Date(30),
                new ArrayList<>());

        HashMap<String, CalibratedBeacon> beaconMap = new HashMap<>();
        beaconMap.put(beacon.getDeviceId(), beacon);

        String beaconMapString = new Gson().toJson(beaconMap);
        when(mockStorageLocker.load(StorageLockerType.StorageKey.BEACON)).thenReturn(beaconMapString);

        HashMap<String, CalibratedBeacon> beaconReturnMap = beaconStorageManager.loadAllBeacons();

        Assert.assertEquals(beaconMap.keySet(), beaconReturnMap.keySet());
        verify(mockStorageLocker, times(1)).load(StorageLockerType.StorageKey.BEACON);
        verifyNoMoreInteractions(mockStorageLocker);
    }

    @Test
    void loadAllBeacons_StorageManagerThrows_ReturnEmptyMap() throws NothingToLoadException {
        CalibratedBeacon beacon = new CalibratedBeacon(
                "deviceId",
                "macAddress",
                "deviceName",
                42,
                42,
                new Date(30),
                new ArrayList<>());

        HashMap<String, CalibratedBeacon> beaconMap = new HashMap<>();
        beaconMap.put(beacon.getDeviceId(), beacon);

        String beaconMapString = new Gson().toJson(beaconMap);
        when(mockStorageLocker.load(StorageLockerType.StorageKey.BEACON)).thenThrow(new NothingToLoadException());

        HashMap<String, CalibratedBeacon> beaconReturnMap = beaconStorageManager.loadAllBeacons();

        Assert.assertTrue(beaconReturnMap.isEmpty());
        verify(mockStorageLocker, times(1)).load(StorageLockerType.StorageKey.BEACON);
        verifyNoMoreInteractions(mockStorageLocker);
    }

    @Test
    void loadBeaconById_Success() throws CouldNotFindBeaconByIdException, NothingToLoadException {
        CalibratedBeacon beacon = new CalibratedBeacon(
                "deviceId",
                "macAddress",
                "deviceName",
                42,
                42,
                new Date(30),
                new ArrayList<>());

        HashMap<String, CalibratedBeacon> beaconMap = new HashMap<>();
        beaconMap.put(beacon.getDeviceId(), beacon);

        String beaconMapString = new Gson().toJson(beaconMap);
        when(mockStorageLocker.load(StorageLockerType.StorageKey.BEACON)).thenReturn(beaconMapString);

        CalibratedBeacon returnedBeacon = beaconStorageManager.loadBeaconById(beacon.getDeviceId());

        Assert.assertEquals(beacon.getDeviceId(), returnedBeacon.getDeviceId());
        verify(mockStorageLocker, times(1)).load(StorageLockerType.StorageKey.BEACON);
        verifyNoMoreInteractions(mockStorageLocker);
    }

    @Test
    void loadBeaconById_CouldNotFindBeaconByIdExceotion_WrongDeviceId() throws NothingToLoadException {
        CalibratedBeacon beacon = new CalibratedBeacon(
                "deviceId",
                "macAddress",
                "deviceName",
                42,
                42,
                new Date(30),
                new ArrayList<>());

        HashMap<String, CalibratedBeacon> beaconMap = new HashMap<>();
        beaconMap.put(beacon.getDeviceId(), beacon);

        String beaconMapString = new Gson().toJson(beaconMap);
        when(mockStorageLocker.load(StorageLockerType.StorageKey.BEACON)).thenReturn(beaconMapString);

        Assertions.assertThrows(CouldNotFindBeaconByIdException.class, () -> {
            beaconStorageManager.loadBeaconById("randomId");
        });

        verify(mockStorageLocker, times(1)).load(StorageLockerType.StorageKey.BEACON);
        verifyNoMoreInteractions(mockStorageLocker);
    }

    @Test
    void loadBeaconById_CouldNotFindBeaconByIdExceotion_EmptyMap() throws NothingToLoadException {
        CalibratedBeacon beacon = new CalibratedBeacon(
                "deviceId",
                "macAddress",
                "deviceName",
                42,
                42,
                new Date(30),
                new ArrayList<>());

        HashMap<String, CalibratedBeacon> beaconMap = new HashMap<>();

        String beaconMapString = new Gson().toJson(beaconMap);
        when(mockStorageLocker.load(StorageLockerType.StorageKey.BEACON)).thenReturn(beaconMapString);

        Assertions.assertThrows(CouldNotFindBeaconByIdException.class, () -> {
            beaconStorageManager.loadBeaconById(beacon.getDeviceId());
        });

        verify(mockStorageLocker, times(1)).load(StorageLockerType.StorageKey.BEACON);
        verifyNoMoreInteractions(mockStorageLocker);
    }

    @Test
    void deletebeaconById_Success() throws NothingToLoadException, CouldNotFindBeaconByIdException {
        final String EMPTY_MAP_STRING = "{}";

        CalibratedBeacon beacon = new CalibratedBeacon(
                "deviceId",
                "macAddress",
                "deviceName",
                42,
                42,
                new Date(30),
                new ArrayList<>());

        HashMap<String, CalibratedBeacon> beaconMap = new HashMap<>();
        beaconMap.put(beacon.getDeviceId(), beacon);

        String beaconMapString = new Gson().toJson(beaconMap);
        when(mockStorageLocker.load(StorageLockerType.StorageKey.BEACON)).thenReturn(beaconMapString);

        beaconStorageManager.deleteBeaconById(beacon.getDeviceId());

        verify(mockStorageLocker, times(1)).load(StorageLockerType.StorageKey.BEACON);
        verify(mockStorageLocker, times(1)).store(EMPTY_MAP_STRING, StorageLockerType.StorageKey.BEACON);
        verifyNoMoreInteractions(mockStorageLocker);
    }

    @Test
    void deletebeaconById_CouldNotFindBeaconByIdException() throws NothingToLoadException, CouldNotFindBeaconByIdException {
        CalibratedBeacon beacon = new CalibratedBeacon(
                "deviceId",
                "macAddress",
                "deviceName",
                42,
                42,
                new Date(30),
                new ArrayList<>());

        HashMap<String, CalibratedBeacon> beaconMap = new HashMap<>();

        String beaconMapString = new Gson().toJson(beaconMap);
        when(mockStorageLocker.load(StorageLockerType.StorageKey.BEACON)).thenReturn(beaconMapString);

        Assertions.assertThrows(CouldNotFindBeaconByIdException.class, () -> {
            beaconStorageManager.deleteBeaconById(beacon.getDeviceId());
        });
        verify(mockStorageLocker, times(1)).load(StorageLockerType.StorageKey.BEACON);
        verifyNoMoreInteractions(mockStorageLocker);
    }
}
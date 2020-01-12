package com.bbeacon.managers.storage;


import com.bbeacon.exceptions.CouldNotFindBeaconByIndexException;
import com.bbeacon.exceptions.NothingToLoadException;
import com.bbeacon.models.CalibratedBeacon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BeaconStorageManager implements BeaconStorageManagerType {

    private StorageLockerType storageLocker;

    @Inject
    public BeaconStorageManager(StorageLockerType storageLocker) {
        this.storageLocker = storageLocker;
    }

    @Override
    public void storeBeacon(CalibratedBeacon beacon) {
        Gson gson = new Gson();
        String jsonString;

        Map<String, CalibratedBeacon> beacons = loadAllBeacons();
        beacons.put(beacon.getDeviceId(), beacon);
        jsonString = gson.toJson(beacons);

        storageLocker.store(jsonString, StorageLockerType.StorageKey.BEACON);
    }

    @Override
    public HashMap<String, CalibratedBeacon> loadAllBeacons() {
        Type type = new TypeToken<HashMap<String, CalibratedBeacon>>() {
        }.getType();

        Gson gson = new Gson();
        String jsonString;

        try {
            jsonString = storageLocker.load(StorageLockerType.StorageKey.BEACON);
        } catch (NothingToLoadException nothingToLoad) {
            return new HashMap<>();
        }

        HashMap<String, CalibratedBeacon> beacons = gson.fromJson(jsonString, type);

        return beacons;
    }

    @Override
    public CalibratedBeacon loadBeaconById(String deviceId) throws CouldNotFindBeaconByIndexException {
        Map<String, CalibratedBeacon> beacons = loadAllBeacons();

        if (!beacons.containsKey(deviceId))
            throw new CouldNotFindBeaconByIndexException();

        return beacons.get(deviceId);
    }

    @Override
    public void deleteBeaconById(String deviceId) throws CouldNotFindBeaconByIndexException {
        HashMap<String, CalibratedBeacon> beacons = loadAllBeacons();

        if (!beacons.containsKey(deviceId))
            throw new CouldNotFindBeaconByIndexException();

        beacons.remove(deviceId);
        String jsonString = new Gson().toJson(beacons);

        storageLocker.store(jsonString, StorageLockerType.StorageKey.BEACON);
    }
}
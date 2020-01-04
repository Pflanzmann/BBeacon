package com.bbeacon.managers.Storage;

import com.bbeacon.exceptions.CouldNotFindBeaconByIdException;
import com.bbeacon.models.CalibratedBeacon;

import java.util.HashMap;

public interface BeaconStorageManagerType {
    void storeBeacon(CalibratedBeacon beacon);

    HashMap<String, CalibratedBeacon> loadAllBeacons();

    CalibratedBeacon loadBeaconById(String deviceId) throws CouldNotFindBeaconByIdException;

    void deleteBeaconById(String deviceId) throws CouldNotFindBeaconByIdException;
}

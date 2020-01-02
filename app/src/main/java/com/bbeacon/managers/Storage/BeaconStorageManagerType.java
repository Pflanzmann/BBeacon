package com.bbeacon.managers.Storage;

import com.bbeacon.exceptions.CouldNotFindBeaconByIdException;
import com.bbeacon.models.CalibratedBeacon;

import java.util.List;

public interface BeaconStorageManagerType {
    void storeBeacon(CalibratedBeacon beacon);

    List<CalibratedBeacon> loadAllBeacons();

    CalibratedBeacon loadBeaconById(String deviceId) throws CouldNotFindBeaconByIdException;

    void deletebeaconById(String deviceId) throws CouldNotFindBeaconByIdException;
}

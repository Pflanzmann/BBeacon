package com.bbeacon.managers.storage;

import com.bbeacon.exceptions.CouldNotFindBeaconByIndexException;
import com.bbeacon.models.CalibratedBeacon;

import java.util.HashMap;

/**
 * Handles the beacon storage
 */
public interface BeaconStorageManagerType {

    /**
     * Adds a beacon to the storage
     *
     * @param beacon The beacon that should be added in the storage
     */
    void storeBeacon(CalibratedBeacon beacon);

    /**
     * Load all beacons and returns them
     *
     * @return A HashMap from CalibratedBeacons by their DeviceId as Key
     */
    HashMap<String, CalibratedBeacon> loadAllBeacons();

    /**
     * Loads one beacon by his DeviceId
     *
     * @param deviceId The DeviceId of the desired beacon
     * @return The desired Beacon
     * @throws CouldNotFindBeaconByIndexException Throws if the beacon could not be found
     */
    CalibratedBeacon loadBeaconById(String deviceId) throws CouldNotFindBeaconByIndexException;

    /**
     * Deletes the beacon from the storage
     *
     * @param deviceId The DeviceId of the desired beacon
     * @throws CouldNotFindBeaconByIndexException Throws if the beacon could not be found
     */
    void deleteBeaconById(String deviceId) throws CouldNotFindBeaconByIndexException;
}

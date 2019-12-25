package com.bbeacon.managers.Storage;

import com.bbeacon.models.CalibratedBeacon;

import java.util.List;

public interface BeaconStorageManagerType {
    public void storeBeacon(CalibratedBeacon beacon);

    public List<CalibratedBeacon> loadAllBeacons();

    public void loadBeaconBy(String macAddress);
}

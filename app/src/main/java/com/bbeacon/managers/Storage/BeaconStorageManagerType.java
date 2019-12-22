package com.bbeacon.managers.Storage;

import android.content.Context;

import com.bbeacon.models.Beacon;

import java.util.List;

public interface BeaconStorageManagerType {
    public void storeBeacon(Context context, Beacon beacon);

    public List<Beacon> loadAllBeacons(Context context);

    public void loadBeaconBy(Context context, String macAddress);
}

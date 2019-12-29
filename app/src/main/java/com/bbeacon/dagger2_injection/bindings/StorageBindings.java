package com.bbeacon.dagger2_injection.bindings;

import com.bbeacon.managers.Storage.BeaconStorageManager;
import com.bbeacon.managers.Storage.BeaconStorageManagerType;
import com.bbeacon.managers.Storage.RoomStorageManager;
import com.bbeacon.managers.Storage.RoomStorageManagerType;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class StorageBindings {

    @Binds
    public abstract BeaconStorageManagerType bindBeaconStorageManager(BeaconStorageManager beaconStorageManager);

    @Binds
    public abstract RoomStorageManagerType bindRoomStorageManager(RoomStorageManager roomStorageManager);
}

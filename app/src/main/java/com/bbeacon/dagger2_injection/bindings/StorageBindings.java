package com.bbeacon.dagger2_injection.bindings;

import com.bbeacon.managers.storage.BeaconStorageManager;
import com.bbeacon.managers.storage.BeaconStorageManagerType;
import com.bbeacon.managers.storage.SingleRoomStorageManager;
import com.bbeacon.managers.storage.SingleRoomStorageManagerType;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class StorageBindings {

    @Binds
    public abstract BeaconStorageManagerType bindBeaconStorageManager(BeaconStorageManager beaconStorageManager);

    @Binds
    public abstract SingleRoomStorageManagerType bindRoomStorageManager(SingleRoomStorageManager singleRoomStorageManager);
}

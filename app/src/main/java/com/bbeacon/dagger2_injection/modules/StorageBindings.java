package com.bbeacon.dagger2_injection.modules;

import com.bbeacon.managers.Storage.BeaconStorageManager;
import com.bbeacon.managers.Storage.BeaconStorageManagerType;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class StorageBindings {

    @Binds
    public abstract BeaconStorageManagerType provideBeaconStorageManager(BeaconStorageManager beaconStorageManager);
}

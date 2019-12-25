package com.bbeacon.dagger2_injection.bindings;

import com.bbeacon.managers.BleManager;
import com.bbeacon.managers.BleManagerType;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ManagerBindings {

    @Binds
    public abstract BleManagerType provideBleManagerType(BleManager bleManager);
}

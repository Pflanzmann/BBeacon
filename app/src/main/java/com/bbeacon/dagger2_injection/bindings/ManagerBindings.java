package com.bbeacon.dagger2_injection.bindings;

import com.bbeacon.managers.BleManager;
import com.bbeacon.managers.BleManagerType;
import com.bbeacon.managers.RoomManager;
import com.bbeacon.managers.RoomManagerType;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ManagerBindings {

    @Binds
    public abstract BleManagerType bindBleManager(BleManager bleManager);

    @Binds
    public abstract RoomManagerType bindRoomManager(RoomManager roomManager);
}

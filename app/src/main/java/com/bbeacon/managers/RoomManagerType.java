package com.bbeacon.managers;

import com.bbeacon.models.BeaconPosition;
import com.bbeacon.models.CalibratedBeacon;
import com.bbeacon.models.Room;

public interface RoomManagerType {

    Room getRoom();

    void setPositionOn(int index, BeaconPosition position);

    CalibratedBeacon getBeaconByPositionIndexOrNull(int index);
}

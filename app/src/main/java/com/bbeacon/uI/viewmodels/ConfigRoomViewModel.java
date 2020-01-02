package com.bbeacon.uI.viewmodels;

import android.util.Log;

import com.bbeacon.exceptions.PositionIndexOutOfBound;
import com.bbeacon.managers.RoomManagerType;
import com.bbeacon.managers.Storage.BeaconStorageManagerType;
import com.bbeacon.models.CalibratedBeacon;
import com.bbeacon.models.PositionedBeacon;
import com.bbeacon.models.Room;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfigRoomViewModel extends ViewModel {

    private MutableLiveData<Room> currentRoom = new MutableLiveData<Room>();

    private RoomManagerType roomManagerType;
    private BeaconStorageManagerType beaconStorageManager;

    @Inject
    public ConfigRoomViewModel(RoomManagerType roomManagerType, BeaconStorageManagerType beaconStorageManager) {
        this.roomManagerType = roomManagerType;
        this.beaconStorageManager = beaconStorageManager;

        currentRoom.postValue(roomManagerType.getRoom());
    }

    public LiveData<Room> getCurrentRoom() {
        return currentRoom;
    }

    public void setBeaconOn(int index, CalibratedBeacon calibratedBeacon) {

        int x;
        int y;

        switch (index) {
            case 0:
                x = 0;
                y = 0;
                break;

            case 1:
                x = 1;
                y = 0;
                break;

            case 2:
                x = 0;
                y = 1;
                break;

            case 3:
                x = 1;
                y = 1;
                break;

            default:
                Log.d("OwnLog", "setBeaconOn: default: " + index);
                return;
        }

        PositionedBeacon positionedBeacon = new PositionedBeacon(calibratedBeacon, x, y);

        try {
            roomManagerType.setPositionOn(index, positionedBeacon);
        } catch (PositionIndexOutOfBound positionIndexOutOfBound) {
            positionIndexOutOfBound.printStackTrace();
        }

        currentRoom.postValue(roomManagerType.getRoom());
    }
}

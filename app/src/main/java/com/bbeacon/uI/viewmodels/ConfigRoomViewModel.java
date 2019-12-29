package com.bbeacon.uI.viewmodels;

import com.bbeacon.managers.RoomManagerType;
import com.bbeacon.models.BeaconPosition;
import com.bbeacon.models.Room;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfigRoomViewModel extends ViewModel {

    private MutableLiveData<Room> currentRoom = new MutableLiveData<Room>();

    private RoomManagerType roomManagerType;

    @Inject
    public ConfigRoomViewModel(RoomManagerType roomManagerType) {
        this.roomManagerType = roomManagerType;

        currentRoom.postValue(roomManagerType.getRoom());
    }

    public LiveData<Room> getCurrentRoom() {
        return currentRoom;
    }

    public void setBeaconOn(int index, String beaconId) {

        int x;
        int y;

        switch (index) {
            case 1:
                x = 0;
                y = 0;
                break;

            case 2:
                x = 1;
                y = 0;
                break;

            case 3:
                x = 0;
                y = 1;
                break;

            case 4:
                x = 1;
                y = 1;
                break;

            default:
                x = -1;
                y = -1;
                break;
        }

        BeaconPosition position = new BeaconPosition(x, y, beaconId);

        roomManagerType.setPositionOn(index, position);

        currentRoom.postValue(roomManagerType.getRoom());
    }
}

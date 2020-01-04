package com.bbeacon.uI.viewmodels;

import android.util.Log;

import com.bbeacon.exceptions.CouldNotFindBeaconByIdException;
import com.bbeacon.managers.RoomManagerType;
import com.bbeacon.managers.Storage.BeaconStorageManagerType;
import com.bbeacon.models.CalibratedBeacon;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KnownBeaconListViewModel extends ViewModel {

    private MutableLiveData<ArrayList<CalibratedBeacon>> knownBeacons = new MutableLiveData<>(new ArrayList<CalibratedBeacon>());

    BeaconStorageManagerType storageManager;
    private RoomManagerType roomManager;

    @Inject
    public KnownBeaconListViewModel(BeaconStorageManagerType storageManager, RoomManagerType roomManager) {
        this.storageManager = storageManager;
        this.roomManager = roomManager;
    }

    public LiveData<ArrayList<CalibratedBeacon>> getKnownBeacons() {
        return knownBeacons;
    }

    public void loadCalibratedBeacons() {
        knownBeacons.postValue(new ArrayList<>(storageManager.loadAllBeacons().values()));
    }

    public void deleteCalibratedBeacon(String deviceId) {
        try {
            storageManager.deleteBeaconById(deviceId);
        } catch (CouldNotFindBeaconByIdException e) {
            Log.e("OwnTag", "deleteCalibratedBeacon: ", e);
        }

        roomManager.removeBeaconFromRoomById(deviceId);
    }
}

package com.bbeacon.uI.viewmodels;

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

    @Inject
    public KnownBeaconListViewModel(BeaconStorageManagerType storageManager) {
        this.storageManager = storageManager;
    }

    public LiveData<ArrayList<CalibratedBeacon>> getKnownBeacons() {
        return knownBeacons;
    }

    public void loadCalibratedBeacons(){
        knownBeacons.postValue(new ArrayList<>(storageManager.loadAllBeacons()));
    }
}

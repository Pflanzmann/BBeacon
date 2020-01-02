package com.bbeacon.managers.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bbeacon.exceptions.CouldNotFindBeaconByIdException;
import com.bbeacon.models.CalibratedBeacon;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BeaconStorageManager implements BeaconStorageManagerType {

    private final String PREFS_KEY = "bbeacon";
    private final String DEFAULT_VALUE = "";

    private final String BEACONS_KEY = "beacons";

    Context context;

    @Inject
    public BeaconStorageManager(Context context) {
        this.context = context;
    }

    @Override
    public void storeBeacon(CalibratedBeacon beacon) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        BeaconsContainer container = gson.fromJson(prefs.getString(BEACONS_KEY, DEFAULT_VALUE), BeaconsContainer.class);

        if (container != null && container.beacons != null) {
            if (!container.beacons.containsKey(beacon.getDeviceId())) {
                container.beacons.put(beacon.getDeviceId(), beacon);
                Log.d("OwnLog", "storeBeacon: put");
            } else {
                container.beacons.replace(beacon.getDeviceId(), beacon);
                Log.d("OwnLog", "storeBeacon: replace");
            }
        } else {
            container = new BeaconsContainer(new HashMap<>());
            container.beacons.put(beacon.getDeviceId(), beacon);
            Log.d("OwnLog", "storeBeacon: new ocntainer and put");
        }

        prefs.edit().putString(BEACONS_KEY, gson.toJson(container, BeaconsContainer.class)).apply();
    }

    @Override
    public List<CalibratedBeacon> loadAllBeacons() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        BeaconsContainer container = gson.fromJson(prefs.getString(BEACONS_KEY, DEFAULT_VALUE), BeaconsContainer.class);

        if (container != null && container.beacons != null) {
            Map<String, CalibratedBeacon> beacons = container.beacons;
            return new ArrayList<>(beacons.values());
        }

        return new ArrayList<>();

    }

    @Override
    public CalibratedBeacon loadBeaconById(String deviceId) throws CouldNotFindBeaconByIdException {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        BeaconsContainer container = gson.fromJson(prefs.getString(BEACONS_KEY, DEFAULT_VALUE), BeaconsContainer.class);

        if (container != null && container.beacons != null) {
            Map<String, CalibratedBeacon> beacons = container.beacons;

            return beacons.get(deviceId);
        }

        throw new CouldNotFindBeaconByIdException();
    }

    private class BeaconsContainer {
        private Map<String, CalibratedBeacon> beacons;

        private BeaconsContainer(Map<String, CalibratedBeacon> beacons) {
            this.beacons = beacons;
        }
    }

    @Override
    public void deletebeaconById(String deviceId) throws CouldNotFindBeaconByIdException {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        BeaconsContainer container = gson.fromJson(prefs.getString(BEACONS_KEY, DEFAULT_VALUE), BeaconsContainer.class);
        if (container != null && container.beacons != null && container.beacons.containsKey(deviceId)) {

            container.beacons.remove(deviceId);

            prefs.edit().putString(BEACONS_KEY, gson.toJson(container, BeaconsContainer.class)).apply();
            return;
        }

        throw new CouldNotFindBeaconByIdException();

    }
}

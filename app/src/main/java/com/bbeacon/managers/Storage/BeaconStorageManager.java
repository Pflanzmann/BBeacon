package com.bbeacon.managers.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
            Log.d("OwnLog", "storeBeacon: new ocntainer");
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
    public CalibratedBeacon loadBeaconById(String deviceId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        BeaconsContainer container = gson.fromJson(prefs.getString(BEACONS_KEY, DEFAULT_VALUE), BeaconsContainer.class);

        if (container != null && container.beacons != null) {
            Map<String, CalibratedBeacon> beacons = container.beacons;

            return beacons.get(deviceId);
        }

        return null;

    }

    private class BeaconsContainer {
        private Map<String, CalibratedBeacon> beacons;

        private BeaconsContainer(Map<String, CalibratedBeacon> beacons) {
            this.beacons = beacons;
        }
    }
}

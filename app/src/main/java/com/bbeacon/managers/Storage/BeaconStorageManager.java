package com.bbeacon.managers.Storage;

import android.content.Context;
import android.content.SharedPreferences;

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
    private final String DEFAULT_KEY = "";

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

        BeaconsContainer container = gson.fromJson(prefs.getString(BEACONS_KEY, DEFAULT_KEY), BeaconsContainer.class);

        if (container != null && container.beacons != null) {
            container.beacons.put(beacon.getMacAddress(), beacon);
        } else
            container = new BeaconsContainer(new HashMap<>());

        prefs.edit().putString(BEACONS_KEY, gson.toJson(container, BeaconsContainer.class)).apply();
    }

    @Override
    public List<CalibratedBeacon> loadAllBeacons() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();


        BeaconsContainer container = gson.fromJson(prefs.getString(BEACONS_KEY, DEFAULT_KEY), BeaconsContainer.class);

        if (container != null && container.beacons != null) {
            Map<String, CalibratedBeacon> beacons = gson.fromJson(prefs.getString(BEACONS_KEY, DEFAULT_KEY), BeaconsContainer.class).beacons;
            return new ArrayList<>(beacons.values());
        }

        return new ArrayList<>();

    }

    @Override
    public void loadBeaconBy(String macAddress) {

    }

    private class BeaconsContainer {
        private Map<String, CalibratedBeacon> beacons;

        private BeaconsContainer(Map<String, CalibratedBeacon> beacons) {
            this.beacons = beacons;
        }
    }
}

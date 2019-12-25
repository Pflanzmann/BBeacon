package com.bbeacon.managers.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.bbeacon.models.CalibratedBeacon;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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

        Map<String, CalibratedBeacon> beacons = gson.fromJson(prefs.getString(BEACONS_KEY, DEFAULT_KEY), BeaconsContainer.class).beacons;

        beacons.put(beacon.getMacAddress(), beacon);

        prefs.edit().putString(BEACONS_KEY, gson.toJson(new BeaconsContainer(beacons), BeaconsContainer.class)).apply();
    }

    @Override
    public List<CalibratedBeacon> loadAllBeacons() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        Map<String, CalibratedBeacon> beacons = gson.fromJson(prefs.getString(BEACONS_KEY, DEFAULT_KEY), BeaconsContainer.class).beacons;

        return new ArrayList<CalibratedBeacon>(beacons.values());
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

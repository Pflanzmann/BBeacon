package com.bbeacon.managers.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.bbeacon.models.Beacon;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeaconStorageManager implements BeaconStorageManagerType {

    private final String PREFS_KEY = "bbeacon";
    private final String DEFAULT_KEY = "";

    private final String BEACONS_KEY = "beacons";


    @Override
    public void storeBeacon(Context context, Beacon beacon) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        Map<String, Beacon> beacons = gson.fromJson(prefs.getString(BEACONS_KEY, DEFAULT_KEY), BeaconsContainer.class).beacons;

        beacons.put(beacon.getMacAddress(), beacon);

        prefs.edit().putString(BEACONS_KEY, gson.toJson(new BeaconsContainer(beacons), BeaconsContainer.class)).apply();
    }

    @Override
    public List<Beacon> loadAllBeacons(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        Map<String, Beacon> beacons = gson.fromJson(prefs.getString(BEACONS_KEY, DEFAULT_KEY), BeaconsContainer.class).beacons;

        return new ArrayList<Beacon>(beacons.values());
    }

    @Override
    public void loadBeaconBy(Context context, String macAddress) {

    }

    private class BeaconsContainer {
        private Map<String, Beacon> beacons;

        private BeaconsContainer(Map<String, Beacon> beacons) {
            this.beacons = beacons;
        }
    }
}

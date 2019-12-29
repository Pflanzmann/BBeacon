package com.bbeacon.managers.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bbeacon.models.Room;
import com.google.gson.Gson;

import javax.inject.Inject;

public class RoomStorageManager implements RoomStorageManagerType {

    private final String PREFS_KEY = "bbeacon";
    private final String DEFAULT_VALUE = "";

    private final String ROOMS_KEY = "room";

    private Context context;

    @Inject
    public RoomStorageManager(Context context) {
        this.context = context;
    }

    @Override
    public void storeRoom(Room room) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        prefs.edit().putString(ROOMS_KEY, gson.toJson(room)).apply();
    }

    @Override
    public Room loadRoom() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        Room room = gson.fromJson(prefs.getString(ROOMS_KEY, DEFAULT_VALUE), Room.class);

        Log.d("OwnLog", "loadRoom prefs: " + prefs.getString(ROOMS_KEY, DEFAULT_VALUE));
        if (room != null && room.getBeaconPositions() != null)
            return room;

        Log.d("OwnLog", "loadRoom: empty room");

        return new Room(4);
    }
}

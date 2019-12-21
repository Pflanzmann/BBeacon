package com.bbeacon;

import android.app.Application;
import android.util.Log;

public class BBeaconApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("OwnLog", "App start");
    }
}

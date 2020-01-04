package com.bbeacon.dagger2_injection.modules;


import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilityModule {

    @Provides
    public BluetoothAdapter provideBluetoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    @Provides
    public Context provideApplicationContext(Application application){
        return application.getApplicationContext();
    }

    @Provides
    public SharedPreferences provideSharedPreferences(Context context){
        return context.getSharedPreferences("bbeacon", Context.MODE_PRIVATE);
    }
}

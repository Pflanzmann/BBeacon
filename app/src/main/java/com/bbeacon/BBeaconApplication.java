package com.bbeacon;

import com.bbeacon.dagger2_injection.setup.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;


public class BBeaconApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //DaggerAppComponent.builder().application(this).build();
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
//    return null;
    }


}

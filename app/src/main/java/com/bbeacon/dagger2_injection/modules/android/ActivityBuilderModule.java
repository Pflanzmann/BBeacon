package com.bbeacon.dagger2_injection.modules.android;

import com.bbeacon.uI.activitys.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();
}

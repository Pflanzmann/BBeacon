package com.bbeacon.dagger2_injection.modules.android;

import com.bbeacon.uI.fragments.BeaconFinderFragment;
import com.bbeacon.uI.fragments.CalibrateBeaconFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuilderModule {

    @ContributesAndroidInjector(
            modules = ViewModelModule.class
    )
    abstract CalibrateBeaconFragment contributeCalibrateBeaconFragment();

    @ContributesAndroidInjector(
            modules = ViewModelModule.class
    )
    abstract BeaconFinderFragment contributeBeaconFinderFragment();

}

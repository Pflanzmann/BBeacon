package com.bbeacon.dagger2_injection.modules;

import com.bbeacon.uI.fragments_ViewModels.BeaconFinderFragment;
import com.bbeacon.uI.fragments_ViewModels.CalibrateBeaconFragment;

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

package com.bbeacon.dagger2_injection.modules.android;

import com.bbeacon.uI.fragments.ConfigRoomFragment;
import com.bbeacon.uI.fragments.DefineBeaconFragment;
import com.bbeacon.uI.fragments.FindBeaconFragment;
import com.bbeacon.uI.fragments.CalibrateBeaconFragment;
import com.bbeacon.uI.fragments.KnownBeaconListFragment;
import com.bbeacon.uI.fragments.LocationFragment;
import com.bbeacon.uI.fragments.MainMenuFragment;

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
    abstract FindBeaconFragment contributeBeaconFinderFragment();

    @ContributesAndroidInjector(
            modules = ViewModelModule.class
    )
    abstract ConfigRoomFragment contributeConfigRoomFragment();

    @ContributesAndroidInjector(
            modules = ViewModelModule.class
    )
    abstract KnownBeaconListFragment contributeKnownBeaconListFragment();

    @ContributesAndroidInjector(
            modules = ViewModelModule.class
    )
    abstract MainMenuFragment contributeMainMenuFragment();

    @ContributesAndroidInjector(
            modules = ViewModelModule.class
    )
    abstract LocationFragment contributeLocationFragment();


    @ContributesAndroidInjector(
            modules = ViewModelModule.class
    )
    abstract DefineBeaconFragment contributeDefineBeaconFragment();
}

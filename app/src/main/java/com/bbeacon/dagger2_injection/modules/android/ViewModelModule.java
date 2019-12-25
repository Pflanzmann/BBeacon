package com.bbeacon.dagger2_injection.modules.android;


import com.bbeacon.dagger2_injection.setup.ViewModelKey;
import com.bbeacon.uI.viewmodels.BeaconFinderViewModel;
import com.bbeacon.uI.viewmodels.CalibrateBeaconViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CalibrateBeaconViewModel.class)
    public abstract ViewModel bindCalibrationViewModel(CalibrateBeaconViewModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(BeaconFinderViewModel.class)
    public abstract ViewModel bindBeaconFinderViewModel(BeaconFinderViewModel viewModel);

}

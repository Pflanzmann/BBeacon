package com.bbeacon.dagger2_injection.modules;


import com.bbeacon.dagger2_injection.setup.ViewModelKey;
import com.bbeacon.uI.fragments_ViewModels.BeaconFinderViewModel;
import com.bbeacon.uI.fragments_ViewModels.CalibrateBeaconViewModel;

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

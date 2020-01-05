package com.bbeacon.dagger2_injection.modules.android;


import com.bbeacon.dagger2_injection.setup.ViewModelKey;
import com.bbeacon.uI.viewmodels.CalibrateBeaconViewModel;
import com.bbeacon.uI.viewmodels.ConfigRoomViewModel;
import com.bbeacon.uI.viewmodels.FindBeaconViewModel;
import com.bbeacon.uI.viewmodels.KnownBeaconListViewModel;
import com.bbeacon.uI.viewmodels.LocationViewModel;

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
    @ViewModelKey(FindBeaconViewModel.class)
    public abstract ViewModel bindBeaconFinderViewModel(FindBeaconViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ConfigRoomViewModel.class)
    public abstract ViewModel bindConfigRoomViewModel(ConfigRoomViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(KnownBeaconListViewModel.class)
    public abstract ViewModel bindKnownBeaconListViewModel(KnownBeaconListViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LocationViewModel.class)
    public abstract ViewModel bindLocationViewModel(LocationViewModel viewModel);
}

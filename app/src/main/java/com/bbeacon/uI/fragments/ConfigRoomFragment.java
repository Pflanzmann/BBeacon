package com.bbeacon.uI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbeacon.R;
import com.bbeacon.dagger2_injection.setup.ViewModelProviderFactory;
import com.bbeacon.uI.viewmodels.ConfigRoomViewModel;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dagger.android.support.DaggerFragment;

public class ConfigRoomFragment extends DaggerFragment {

    private ConfigRoomViewModel viewModel;

    public static ConfigRoomFragment newInstance() {
        return new ConfigRoomFragment();
    }

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.config_room_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, providerFactory).get(ConfigRoomViewModel.class);

        getView().findViewById(R.id.selectBeaconButton).setOnClickListener(view -> onChooseBeacon());
    }

    public void onChooseBeacon(){
        Navigation.findNavController(getView()).navigate(R.id.action_configRoom_to_knownBeaconList);
    }

}

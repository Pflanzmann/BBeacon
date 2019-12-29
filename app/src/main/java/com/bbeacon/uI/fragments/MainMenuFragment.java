package com.bbeacon.uI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbeacon.R;
import com.bbeacon.dagger2_injection.setup.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import dagger.android.support.DaggerFragment;

public class MainMenuFragment extends DaggerFragment {

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_menu_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().findViewById(R.id.startLocatingButton).setOnClickListener(view -> onStartLocatingButton());
        getView().findViewById(R.id.configRoomButton).setOnClickListener(view -> onConfigRoomButton());
        getView().findViewById(R.id.showKnownBeaconsButton).setOnClickListener(view -> onShowBeaconsButton());
    }

    public void onStartLocatingButton() {
        Navigation.findNavController(getView())
                .navigate(MainMenuFragmentDirections.actionMainMenuToLocation());
    }

    public void onShowBeaconsButton() {
        Navigation.findNavController(getView())
                .navigate(MainMenuFragmentDirections.actionMainMenuToKnownBeaconList());
    }

    public void onConfigRoomButton() {
        Navigation.findNavController(getView())
                .navigate(MainMenuFragmentDirections.actionMainMenuToConfigRoom());
    }
}

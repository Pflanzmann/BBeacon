package com.bbeacon.uI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.bbeacon.R;
import com.bbeacon.dagger2_injection.setup.ViewModelProviderFactory;
import com.bbeacon.models.UncalibratedBeacon;
import com.bbeacon.uI.viewmodels.DefineBeaconViewModel;
import com.google.android.material.textfield.TextInputLayout;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dagger.android.support.DaggerFragment;

public class DefineBeaconFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private DefineBeaconViewModel viewModel;

    DefineBeaconFragmentArgs args;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            args = DefineBeaconFragmentArgs.fromBundle(getArguments());
        }

        return inflater.inflate(R.layout.define_beacon_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(DefineBeaconViewModel.class);

        getView().findViewById(R.id.defineBeaconStartButton).setOnClickListener(view -> onStartButton());
    }

    private void onStartButton() {
        TextInputLayout inputText = getView().findViewById(R.id.nameTextInput);
        SeekBar seekbarMeasurement = getView().findViewById(R.id.measurementSeekBar);
        SeekBar seekbarStep = getView().findViewById(R.id.stepSeekBar);

        UncalibratedBeacon uncalibratedBeacon = new UncalibratedBeacon(
                args.getUnknownBeacon().getMacAddress(),
                args.getUnknownBeacon().getDeviceName(),
                inputText.getEditText().getText().toString(),
                seekbarStep.getProgress(),
                seekbarMeasurement.getProgress());

        Navigation.findNavController(getView())
                .navigate(DefineBeaconFragmentDirections.actionDefineBeaconToCalibrateBeacon(uncalibratedBeacon));
    }

}

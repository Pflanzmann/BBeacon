package com.bbeacon.uI.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.bbeacon.R;
import com.bbeacon.models.UncalibratedBeacon;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import dagger.android.support.DaggerFragment;

public class DefineBeaconFragment extends DaggerFragment {

    DefineBeaconFragmentArgs args;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.define_beacon_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getArguments() != null) {
            args = DefineBeaconFragmentArgs.fromBundle(getArguments());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().findViewById(R.id.defineBeaconStartButton).setOnClickListener(view -> onStartButton());
    }

    private void onStartButton() {
        TextInputLayout inputText = getView().findViewById(R.id.nameTextInput);
        SeekBar seekbarMeasurement = getView().findViewById(R.id.measurementSeekBar);
        SeekBar seekbarStep = getView().findViewById(R.id.stepSeekBar);

        UncalibratedBeacon uncalibratedBeacon = new UncalibratedBeacon(
                inputText.getEditText().getText().toString(),
                args.getUnknownBeacon().getMacAddress(),
                args.getUnknownBeacon().getDeviceName(),
                seekbarMeasurement.getProgress(),
                seekbarStep.getProgress());

        Navigation.findNavController(getView())
                .navigate(DefineBeaconFragmentDirections.actionDefineBeaconToCalibrateBeacon(uncalibratedBeacon));
    }

}

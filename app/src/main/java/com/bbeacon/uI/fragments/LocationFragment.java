package com.bbeacon.uI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbeacon.R;
import com.bbeacon.dagger2_injection.setup.ViewModelProviderFactory;
import com.bbeacon.uI.viewmodels.LocationViewModel;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class LocationFragment extends DaggerFragment {

    private LocationViewModel viewModel;

    public static LocationFragment newInstance() {
        return new LocationFragment();
    }

    TextView text;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.location_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, providerFactory).get(LocationViewModel.class);

        text = getView().findViewById(R.id.distanceTextView);

        viewModel.getCurrentXCoordinates().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float value) {
                onTextChanged(String.valueOf(value));
            }
        });

        viewModel.getCurrentYCoordinates().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float value) {
                onTextChanged(String.valueOf(value));
            }
        });

        viewModel.startLocating();
    }

    private void onTextChanged(String textRange) {
        text.setText(textRange);
    }
}

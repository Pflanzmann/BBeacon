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
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class LocationFragment extends DaggerFragment {

    private LocationViewModel viewModel;

    public static LocationFragment newInstance() {
        return new LocationFragment();
    }

    TextView testText;

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

        testText = getView().findViewById(R.id.testTextView);

        viewModel.getCurrentUserPosition().observe(getViewLifecycleOwner(), userPosition -> {

        });

        viewModel.getCurrentTest().observe(getViewLifecycleOwner(), text -> {
            onTextChanged(String.valueOf(text));
        });

        viewModel.startLocating();
    }

    private void onTextChanged(String textRange) {
        testText.setText(textRange);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (viewModel != null)
            viewModel.stopLocating();
    }
}

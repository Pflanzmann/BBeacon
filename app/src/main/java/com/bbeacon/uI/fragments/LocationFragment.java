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

    TextView testText1;
    TextView testText2;

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

        testText1 = getView().findViewById(R.id.testTextView);
        testText2 = getView().findViewById(R.id.testTextView2);

        viewModel.getCurrentTest1().observe(getViewLifecycleOwner(), text -> {
            onTextChanged1(String.valueOf(text));
        });
        viewModel.getCurrentTest2().observe(getViewLifecycleOwner(), text -> {
            onTextChanged2(String.valueOf(text));
        });

        viewModel.startLocating();
    }

    private void onTextChanged1(String textRange) {
        testText1.setText(textRange);
    }
    private void onTextChanged2(String textRange) {
        testText2.setText(textRange);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (viewModel != null)
            viewModel.stopLocating();
    }
}

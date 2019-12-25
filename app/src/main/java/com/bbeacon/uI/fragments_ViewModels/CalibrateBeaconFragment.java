package com.bbeacon.uI.fragments_ViewModels;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bbeacon.R;
import com.bbeacon.dagger2_injection.setup.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class CalibrateBeaconFragment extends DaggerFragment {

    private CalibrateBeaconViewModel viewModel;
    private ProgressBar progressBar;
    private TextView stepText;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calibrate_beacon_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        viewModel = ViewModelProviders.of(this).get(CalibrateBeaconViewModel.class);

        viewModel = ViewModelProviders.of(this, providerFactory).get(CalibrateBeaconViewModel.class);

        Button startButton = getView().findViewById(R.id.startButton);
        progressBar = getView().findViewById(R.id.progressBar);
        stepText = getView().findViewById(R.id.parameterText);

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                calibrationButtonPressed();
            }
        });

        viewModel.getCurrentState().observe(getViewLifecycleOwner(), new Observer<CalibrateBeaconViewModel.CalibrationState>() {
            @Override
            public void onChanged(CalibrateBeaconViewModel.CalibrationState calibrationState) {
                handleCalibrationState(calibrationState);
            }
        });

        viewModel.getCurrentProgress().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("OwnLog", "this is the progress: " + integer);
            }
        });

        viewModel.getCurrentStep().observe(getViewLifecycleOwner(), integer -> {
            stepText.setText(integer + " Meter");
        });
    }

    public void calibrationButtonPressed() {
        viewModel.calibrate(
                getArguments().getString("calibratingBeacon"));
    }

    private void handleCalibrationState(CalibrateBeaconViewModel.CalibrationState state) {
        switch (state) {
            case IDLE:
                progressBar.setVisibility(View.INVISIBLE);
                break;

            case ERROR:
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Calibration failed, try again", Toast.LENGTH_SHORT).show();
                viewModel.quitErrorReset();
                break;

            case CALIBRATION:
                progressBar.setVisibility(View.VISIBLE);
                break;

            case DONE:
                progressBar.setVisibility(View.INVISIBLE);
                viewModel.quitErrorReset();
                Toast.makeText(getContext(), "Calibration done", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}

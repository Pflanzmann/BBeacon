package com.bbeacon.uI.fragments;

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
import com.bbeacon.uI.viewmodels.CalibrateBeaconViewModel;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dagger.android.support.DaggerFragment;

public class CalibrateBeaconFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private CalibrateBeaconViewModel viewModel;
    private ProgressBar progressBar;
    private TextView stepText;

    private CalibrateBeaconFragmentArgs args;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            args = CalibrateBeaconFragmentArgs.fromBundle(getArguments());
        }

        return inflater.inflate(R.layout.calibrate_beacon_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                Log.d("OwnLog", "TODO: show progress: " + integer);
            }
        });

        viewModel.getCurrentStep().observe(getViewLifecycleOwner(), integer -> {
            stepText.setText(integer + " Meter");
        });
    }

    public void calibrationButtonPressed() {
        viewModel.calibrate(args.getUncalibratedbeacon());
    }

    private void handleCalibrationState(CalibrateBeaconViewModel.CalibrationState state) {
        switch (state) {
            case IDLE:
                progressBar.setVisibility(View.INVISIBLE);
                break;

            case ERROR:
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Calibration step failed, try again", Toast.LENGTH_SHORT).show();
                viewModel.quitError();
                break;

            case CALIBRATION:
                progressBar.setVisibility(View.VISIBLE);
                break;

            case READY:
                progressBar.setVisibility(View.INVISIBLE);
                break;

            case DONE:
                progressBar.setVisibility(View.INVISIBLE);
                viewModel.quitError();

                Navigation.findNavController(getView()).navigate(R.id.action_calibrateBeacon_to_knownBeaconList);
                break;
        }

    }
}

package com.bbeacon.uI.fragments_ViewModels;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bbeacon.R;
import com.bbeacon.models.TaskSuccessfulCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class calibrateBeaconFragment extends Fragment {

    private CalibrateBeaconViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calibrate_beacon_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CalibrateBeaconViewModel.class);

        Button startButton = getView().findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (getArguments() != null) {
                    viewModel.calibrate(
                            getArguments().getString("calibratingBeacon"),
                            getArguments().getInt("calibrationStep"),
                            new TaskSuccessfulCallback() {
                                @Override
                                public void onTaskFinished() {
                                    Log.d("OwnLog", "finished");

                                    Toast.makeText(getContext(), "FINISHED", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onTaskFailed(String message, Exception e) {
                                    Log.d("OwnLog", "error");

                                    Toast.makeText(getContext(), "ERROR!", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
            }
        });
    }
}

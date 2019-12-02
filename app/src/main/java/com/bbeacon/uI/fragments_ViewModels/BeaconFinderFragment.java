package com.bbeacon.uI.fragments_ViewModels;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbeacon.R;
import com.bbeacon.models.UnknownBeacon;

import java.util.ArrayList;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class BeaconFinderFragment extends Fragment {

    private BeaconFinderViewModel viewModel;
    TextView textview;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.beacon_finder_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(BeaconFinderViewModel.class);
        // TODO: Use the ViewModel);

        textview = (TextView) getView().findViewById(R.id.exampleText);

        viewModel.findBluetoothDevices();

        viewModel.getFoundBLEDevices().observe(this, new Observer<ArrayList<UnknownBeacon>>() {
            @Override
            public void onChanged(ArrayList<UnknownBeacon> unknownBeacons) {

                String text = "";

                for (UnknownBeacon unknownBeacon: unknownBeacons) {
                    text += unknownBeacon.getMacAddress() + "\t" + unknownBeacon.getDeviceName() + "\n";
                }

                text += "---------------";
                textview.setText(text);
                Log.d("OwnLog", "Something changed");
            }
        });
    }
}

package com.bbeacon.uI.fragments_ViewModels;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbeacon.R;
import com.bbeacon.models.UnknownBeacon;
import com.bbeacon.uI.RecyclerViewAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BeaconFinderFragment extends Fragment {

    private BeaconFinderViewModel viewModel;
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.beacon_finder_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(BeaconFinderViewModel.class);

        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), viewModel, getViewLifecycleOwner());

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.findBluetoothDevices();
    }
}

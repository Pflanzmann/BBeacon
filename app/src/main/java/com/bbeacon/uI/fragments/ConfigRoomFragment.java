package com.bbeacon.uI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbeacon.R;
import com.bbeacon.dagger2_injection.setup.ViewModelProviderFactory;
import com.bbeacon.models.PositionedBeacon;
import com.bbeacon.models.Room;
import com.bbeacon.uI.viewmodels.ConfigRoomViewModel;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dagger.android.support.DaggerFragment;

public class ConfigRoomFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private ConfigRoomViewModel viewModel;
    private ConfigRoomFragmentArgs args;

    TextView beacon0TextView;
    TextView beacon1TextView;
    TextView beacon2TextView;
    TextView beacon3TextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.config_room_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, providerFactory).get(ConfigRoomViewModel.class);

        if (getArguments() != null) {
            args = ConfigRoomFragmentArgs.fromBundle(getArguments());

            viewModel.setBeaconOn(args.getBeaconToSelect(), args.getBeaconObject());
        }

        viewModel.getCurrentRoom().observe(getViewLifecycleOwner(), room -> {
            assignRoomToUI(room);
        });

        beacon0TextView = getView().findViewById(R.id.selectedBeacon0TextView);
        beacon1TextView = getView().findViewById(R.id.selectedBeacon1TextView);
        beacon2TextView = getView().findViewById(R.id.selectedBeacon2TextView);
        beacon3TextView = getView().findViewById(R.id.selectedBeacon3TextView);

        getView().findViewById(R.id.selectBeacon0).setOnClickListener(view -> onSelectBeacon(0));
        getView().findViewById(R.id.selectBeacon1).setOnClickListener(view -> onSelectBeacon(1));
        getView().findViewById(R.id.selectBeacon2).setOnClickListener(view -> onSelectBeacon(2));
        getView().findViewById(R.id.selectBeacon3).setOnClickListener(view -> onSelectBeacon(3));
    }

    public void onSelectBeacon(int number) {
        Navigation.findNavController(getView())
                .navigate(ConfigRoomFragmentDirections.actionConfigRoomToKnownBeaconList()
                        .setBeaconToSelect(number)
                        .setAccessModifier(KnownBeaconListFragment.accessModifyer.SELECT));
    }

    public void assignRoomToUI(Room room) {
        PositionedBeacon[] positions = room.getBeaconPositions();

        for (int i = 0; i < positions.length; i++) {

            if (positions[i] != null) {

                String beaconId = positions[i].getDeviceId();
                switch (i) {
                    case 0:
                        beacon0TextView.setText(beaconId);
                        break;

                    case 1:
                        beacon1TextView.setText(beaconId);
                        break;

                    case 2:
                        beacon2TextView.setText(beaconId);
                        break;

                    case 3:
                        beacon3TextView.setText(beaconId);
                        break;

                    default:
                        return;
                }
            }
        }
    }

}


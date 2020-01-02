package com.bbeacon.uI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbeacon.R;
import com.bbeacon.dagger2_injection.setup.ViewModelProviderFactory;
import com.bbeacon.models.CalibratedBeacon;
import com.bbeacon.uI.viewmodels.KnownBeaconListViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerFragment;

public class KnownBeaconListFragment extends DaggerFragment {

    public enum accessModifyer {
        SELECT,
        VIEW
    }

    @Inject
    ViewModelProviderFactory providerFactory;

    private KnownBeaconListViewModel viewModel;
    private KnownBeaconListFragment.RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    private KnownBeaconListFragmentArgs args;

    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            args = KnownBeaconListFragmentArgs.fromBundle(getArguments());
        }

        return inflater.inflate(R.layout.known_beacon_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, providerFactory).get(KnownBeaconListViewModel.class);

        getView().findViewById(R.id.createNewBeaconButton).setOnClickListener(view -> onCalibrateNewBeacon());

        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerViewAdapter = new KnownBeaconListFragment.RecyclerViewAdapter();

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        viewModel.loadCalibratedBeacons();
    }

    private void onSelectedBeacon(CalibratedBeacon beacon) {
        Navigation.findNavController(getView())
                .navigate(KnownBeaconListFragmentDirections.actionKnownBeaconListToConfigRoom()
                        .setBeaconObject(beacon)
                        .setBeaconToSelect(args.getBeaconToSelect()));
    }

    private void onCalibrateNewBeacon() {
        Navigation.findNavController(getView())
                .navigate(R.id.action_knownBeaconList_to_findBeacon);
    }

    private void onDeleteBeacon(String deviceId) {
        viewModel.deleteCalibratedBeacon(deviceId);
        viewModel.loadCalibratedBeacons();
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<KnownBeaconListFragment.RecyclerViewAdapter.ViewHolder> {
        ArrayList<CalibratedBeacon> beacons = new ArrayList<>();

        RecyclerViewAdapter() {
            viewModel.getKnownBeacons().observe(getViewLifecycleOwner(), calibratedBeacons -> {
                beacons = calibratedBeacons;
                notifyDataSetChanged();
            });
        }

        @NonNull
        @Override
        public KnownBeaconListFragment.RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.known_beacon_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull KnownBeaconListFragment.RecyclerViewAdapter.ViewHolder holder, int position) {

            holder.calibratedBeacon = beacons.get(position);

            holder.nameText.setText(beacons.get(position).getDeviceId());

            holder.macAddress.setText(beacons.get(position).getMacAddress());
            holder.calibrationDate.setText(df.format(beacons.get(position).getCalibrationDate()));

            if (args.getAccessModifier() == accessModifyer.SELECT)
                holder.itemView.findViewById(R.id.informationLayout).setOnClickListener(view -> onSelectedBeacon(holder.calibratedBeacon));
        }

        @Override
        public int getItemCount() {
            return beacons.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CalibratedBeacon calibratedBeacon;

            TextView nameText;
            TextView macAddress;
            TextView calibrationDate;

            private ViewHolder(@NonNull View itemView) {
                super(itemView);

                nameText = itemView.findViewById(R.id.beaconNameTextView);
                macAddress = itemView.findViewById(R.id.macAddressTextViewOutput);
                calibrationDate = itemView.findViewById(R.id.creationDateTextViewOutput);

                itemView.findViewById(R.id.deleteTextView).setOnClickListener(view -> {
                    onDeleteBeacon(calibratedBeacon.getDeviceId());
                });
            }
        }
    }
}

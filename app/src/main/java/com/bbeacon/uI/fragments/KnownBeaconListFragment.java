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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerFragment;

public class KnownBeaconListFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private KnownBeaconListViewModel viewModel;
    private KnownBeaconListFragment.RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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

    public void onSelectedBeacon() {
        Navigation.findNavController(getView()).navigate(R.id.action_knownBeaconList_to_configRoom);
    }

    public void onCalibrateNewBeacon() {
        Navigation.findNavController(getView()).navigate(R.id.action_knownBeaconList_to_findBeacon);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<KnownBeaconListFragment.RecyclerViewAdapter.ViewHolder> {
        ArrayList<CalibratedBeacon> beacons = new ArrayList<>();

        public RecyclerViewAdapter() {
            viewModel.getKnownBeacons().observe(getViewLifecycleOwner(), new Observer<ArrayList<CalibratedBeacon>>() {
                @Override
                public void onChanged(ArrayList<CalibratedBeacon> calibratedBeacons) {
                    beacons = calibratedBeacons;
                    notifyDataSetChanged();
                }
            });
        }

        @NonNull
        @Override
        public KnownBeaconListFragment.RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.known_beacon_list_item, parent, false);
            KnownBeaconListFragment.RecyclerViewAdapter.ViewHolder holder = new KnownBeaconListFragment.RecyclerViewAdapter.ViewHolder(view);
            return holder;
        }

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm");

        @Override
        public void onBindViewHolder(@NonNull KnownBeaconListFragment.RecyclerViewAdapter.ViewHolder holder, int position) {

            if (beacons.get(position).getDeviceName().length() <= 0)
                holder.nameText.setText(">>Unknown name<<");
            else
                holder.nameText.setText(beacons.get(position).getDeviceName());

            holder.macAddress.setText(beacons.get(position).getMacAddress());
            holder.calibrationDate.setText(df.format(beacons.get(position).getCalibrationDate()));

            if (getArguments().getString("origin").equals("Select"))
                holder.itemView.setOnClickListener(view -> onSelectedBeacon());
        }

        @Override
        public int getItemCount() {
            return beacons.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView nameText;
            TextView macAddress;
            TextView calibrationDate;

            private ViewHolder(@NonNull View itemView) {
                super(itemView);

                nameText = itemView.findViewById(R.id.nameTextView);
                macAddress = itemView.findViewById(R.id.macAddressTextView);
                calibrationDate = itemView.findViewById(R.id.creationDateTextView);
            }
        }
    }
}

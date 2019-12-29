package com.bbeacon.uI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbeacon.R;
import com.bbeacon.dagger2_injection.setup.ViewModelProviderFactory;
import com.bbeacon.models.UnknownBeacon;
import com.bbeacon.uI.viewmodels.FindBeaconViewModel;

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

public class FindBeaconFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private FindBeaconViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.find_beacon_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, providerFactory).get(FindBeaconViewModel.class);

        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter();

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        viewModel.findBluetoothDevices();
    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        ArrayList<UnknownBeacon> beacons = new ArrayList<>();

        public RecyclerViewAdapter() {
            viewModel.getFoundBLEDevices().observe(getViewLifecycleOwner(), new Observer<ArrayList<UnknownBeacon>>() {
                @Override
                public void onChanged(ArrayList<UnknownBeacon> unknownBeacons) {
                    beacons = unknownBeacons;
                    notifyDataSetChanged();
                }
            });
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_beacon_list_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (beacons.get(position).getDeviceName().length() <= 0)
                holder.nameText.setText(">>Unknown name<<");
            else
                holder.nameText.setText(beacons.get(position).getDeviceName());

            holder.macAddress.setText(beacons.get(position).getMacAddress());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.stopBluetoothScan();

                    Navigation.findNavController(getView())
                            .navigate(FindBeaconFragmentDirections.actionFindBeaconToDefineBeacon(
                                    new UnknownBeacon(
                                            holder.macAddress.getText().toString(),
                                            holder.nameText.getText().toString())
                            ));
                }
            });
        }

        @Override
        public int getItemCount() {
            return beacons.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView nameText;
            TextView macAddress;

            private ViewHolder(@NonNull View itemView) {
                super(itemView);

                nameText = itemView.findViewById(R.id.beaconNmaeTextView);
                macAddress = itemView.findViewById(R.id.macAddressTagTextView);
            }
        }
    }
}

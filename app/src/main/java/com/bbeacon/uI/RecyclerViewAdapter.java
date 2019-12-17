package com.bbeacon.uI;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbeacon.R;
import com.bbeacon.models.UnknownBeacon;
import com.bbeacon.uI.fragments_ViewModels.BeaconFinderViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<UnknownBeacon> beacons = new ArrayList<>();
    private BeaconFinderViewModel viewModel;
    private Context context;
    private LifecycleOwner lifecycleOwner;

    public RecyclerViewAdapter(Context context, BeaconFinderViewModel viewModel, LifecycleOwner lifecycleOwner) {
        this.viewModel = viewModel;
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;

        viewModel.getFoundBLEDevices().observe(lifecycleOwner, new Observer<ArrayList<UnknownBeacon>>() {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        if (beacons.get(position).getDeviceName().length() <= 0)
            holder.nameText.setText(">>Unknown<<");
        else
            holder.nameText.setText(beacons.get(position).getDeviceName());

        holder.macAddress.setText(beacons.get(position).getMacAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, holder.nameText.getText(), Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("calibratingBeacon", holder.macAddress.getText().toString());

                viewModel.stopBluetoothScan();

                Navigation.findNavController(view).navigate(R.id.action_beaconFinderFragment_to_calibrateBeaconFragment, bundle);
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

            nameText = itemView.findViewById(R.id.nameTextView);
            macAddress = itemView.findViewById(R.id.macAddressTextView);
        }
    }


}

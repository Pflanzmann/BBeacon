package com.bbeacon.uI;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbeacon.R;
import com.bbeacon.models.UnknownBeacon;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<UnknownBeacon> beacons = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<UnknownBeacon> beacons) {
        this.beacons = beacons;
        this.context = context;
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

                Navigation.findNavController(view).navigate(R.id.action_beaconFinderFragment_to_calibrateBeaconFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beacons.size();
    }

    public void setBeaconList(ArrayList<UnknownBeacon> newBeacons) {
        beacons = newBeacons;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView macAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.nameTextView);
            macAddress = itemView.findViewById(R.id.macAddressTextView);
        }
    }


}

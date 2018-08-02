package com.android.khaled.arduinocontroller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.palaima.smoothbluetooth.Device;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.MyViewHolder> {


    List<Device> devicesList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.device_name);
        }
    }

    public DevicesAdapter(List<Device> devicesList){
        this.devicesList=devicesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Device device=devicesList.get(position);
        Log.e(DevicesAdapter.class.getSimpleName(),device.getName().toString());
        holder.name.setText(device.getName().toString());

    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }
}

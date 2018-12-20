package com.sampullman.solarlight.view.connect;

import android.widget.BaseAdapter;

import android.bluetooth.BluetoothDevice;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sampullman.solarlight.R;

import java.util.ArrayList;

import timber.log.Timber;

public class LeListAdapter extends BaseAdapter {
    private final ArrayList<BluetoothDevice> deviceList = new ArrayList<>();
    private OnLeItemClickListener leItemClickListener;

    public interface OnLeItemClickListener {
        void onLeItemClick(BluetoothDevice device);
    }

    public LeListAdapter() { }

    public void setLeItemClickListener(OnLeItemClickListener leItemClickListener) {
        this.leItemClickListener = leItemClickListener;
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BluetoothDevice device = (BluetoothDevice)getItem(position);
        if (convertView == null) {
            convertView = new LeListItem().build(parent.getContext());
            parent.setFocusable(false);
            convertView.setOnClickListener(v -> {
                Timber.e("CLICKED!");
                if(leItemClickListener != null) {
                    int pos = v.getId();
                    leItemClickListener.onLeItemClick((BluetoothDevice)getItem(pos));
                }
            });
        }

        TextView text = convertView.findViewById(R.id.le_connect_list_item);
        text.setText(device.getName());
        convertView.setId(position);
        return convertView;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addDevice(BluetoothDevice device) {
        deviceList.add(device);
        notifyDataSetChanged();
    }

    public void clear() {
        deviceList.clear();
        notifyDataSetChanged();
    }
}
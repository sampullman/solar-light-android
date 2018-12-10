package com.sampullman.solarlight.view.connect;

import android.widget.BaseAdapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sampullman.solarlight.R;

import java.util.ArrayList;

public class LeListAdapter extends BaseAdapter {

    private final ArrayList<BluetoothDevice> podoList = new ArrayList<>();

    public LeListAdapter() { }

    @Override
    public Object getItem(int position) {
        return podoList.get(position);
    }

    View getView(View convertView, ViewGroup parent, String name) {
        LinearLayout itemView;
        if (convertView == null) {
            itemView = new LeListItem().build(parent.getContext());
            parent.addView(itemView);
        }

        TextView text = convertView.findViewById(R.id.le_connect_list_item);
        text.setText(name);
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BluetoothDevice device = (BluetoothDevice)getItem(position);
        return getView(convertView, parent, device.getName());
    }

    @Override
    public int getCount() {
        return podoList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addDevice(BluetoothDevice device) {
        podoList.add(device);
        notifyDataSetChanged();
    }

    public void clear() {
        podoList.clear();
        notifyDataSetChanged();
    }
}
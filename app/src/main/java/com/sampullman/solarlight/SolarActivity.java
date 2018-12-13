package com.sampullman.solarlight;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sampullman.ble.event.LeEvent;
import com.sampullman.solarlight.SolarLeClient.SolarLeClientListener;

public abstract class SolarActivity extends AppCompatActivity implements SolarLeClientListener {
    public SolarApplication app;
    public SolarLeClient leClient;
    protected BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (SolarApplication)getApplication();
        leClient = app.getLeClient();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        leClient.registerListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        leClient.unregisterListener(this);
    }

    @Override
    public void onEvent(LeEvent event) {}

    @Override
    public void servicesDiscovered() {}
}

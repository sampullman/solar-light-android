package com.sampullman.solarlight;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class SolarActivity extends AppCompatActivity {
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
}

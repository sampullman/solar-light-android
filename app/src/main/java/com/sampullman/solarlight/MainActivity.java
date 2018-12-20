package com.sampullman.solarlight;

import android.os.Bundle;

import com.sampullman.ble.event.LeEvent;
import com.sampullman.solarlight.view.TestPage;

import timber.log.Timber;

import static com.sampullman.solarlight.GattAttributes.SOLAR_ADC1_UUID;

public class MainActivity extends SolarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new TestPage().getView(this));
    }

    @Override
    public void onResume() {
        super.onResume();

        if(btAdapter != null && btAdapter.isEnabled()) {
            SolarLeConnection conn = leClient.getLastConnection();
            conn.readCharacteristic(SOLAR_ADC1_UUID);
        }
    }

    @Override
    public void onEvent(LeEvent event) {
        Timber.e("Got main event");
    }
}

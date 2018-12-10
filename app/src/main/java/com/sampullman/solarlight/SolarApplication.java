package com.sampullman.solarlight;

import android.app.Application;

import timber.log.Timber;

public class SolarApplication extends Application {
    private SolarLeClient leClient;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public SolarLeClient getLeClient() {
        if(leClient == null) {
            leClient = new SolarLeClient(this);
        }
        return leClient;
    }
}

package com.sampullman.solarlight;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import timber.log.Timber;

public class Util {

    public static boolean isLocationDisabled(Context context) {
        int locationMode;

        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

        } catch (Settings.SettingNotFoundException e) {
            Timber.d(e, "Location setting not found");
            return true;
        }

        return locationMode == Settings.Secure.LOCATION_MODE_OFF;
    }

    public static boolean hasPermissions(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void requestPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_ALL);
    }

    private static final String[] permissions = {permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION, permission.BLUETOOTH};
    private static final int PERMISSION_ALL = 300;
}

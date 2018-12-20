package com.sampullman.solarlight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.callbacks.DialogCallbackExtKt;
import com.sampullman.solarlight.view.connect.ConnectPage;
import com.sampullman.solarlight.view.connect.ConnectPage.ConnectPageInterface;
import com.sampullman.solarlight.view.connect.LeListAdapter;
import com.sampullman.solarlight.LeScanner.LeScannerListener;

import timber.log.Timber;
import kotlin.Unit;

public class ConnectActivity extends SolarActivity {
    private static final int LOCATION_REQUEST = 100;

    private MaterialDialog locationDialog, noBleDialog;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private LeScanner leScanner;
    private LeListAdapter leAdapter;
    private ConnectPage connectPage;

    private final ConnectPageInterface pageInterface = new ConnectPageInterface() {
        public LeListAdapter getLeAdapter() {
            return leAdapter;
        }
        public void onItemClick(BluetoothDevice device) {
            connectLeDevice(device);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // display dialog if no bluetooth hardware is present
        if(BluetoothAdapter.getDefaultAdapter() == null) {
            showBluetoothRequiredDialog();
            return;
        }

        if (!Util.hasPermissions(this)) {
            Util.requestPermissions(this);
        }

        leScanner = new LeScanner(leScannerListener);
        leAdapter = new LeListAdapter();
        connectPage = new ConnectPage(pageInterface);

        setContentView(connectPage.getView(this));
        updateTitleText();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        if(requestCode == LOCATION_REQUEST && Util.isLocationDisabled(this)) {
            // TODO -- do something with location disabled info?
        }
        */
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(locationChecker);
        if(noBleDialog != null) {
            noBleDialog.dismiss();
            noBleDialog = null;
        }
        if(BluetoothAdapter.getDefaultAdapter() == null) {
            return;
        }
        leScanner.cancelDiscovery();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            return;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Util.isLocationDisabled(this)
            && locationDialog == null && leAdapter.getCount() == 0) {

            handler.postDelayed(locationChecker, 2500);
        } else {
            locationDialog = null; // Set null in case callback wasn't triggered
        }

        if(btAdapter.isEnabled()) {
            Timber.d("Starting discovery on resume");
            if(leScanner.startDiscovery() == LeScanner.ScanState.LE_UNAVAILABLE && noBleDialog == null) {

                MaterialDialog dialog = new MaterialDialog(this)
                    .title(R.string.popup_title_le_unavailable, null)
                    .message(R.string.popup_message_le_unavailable, null, false, 1)
                    .cancelable(false);

                DialogCallbackExtKt.onDismiss(dialog, d -> {
                    noBleDialog = null;
                    return Unit.INSTANCE;
                });
                dialog.show();
            }
        }
    }

    // Check if location is enabled, and request that the user enables it if not
    // Only necessary for some Android 6.0 phones (and up)
    private final Runnable locationChecker = new Runnable() {
        @Override
        public void run() {
            ConnectActivity a = ConnectActivity.this;

            locationDialog = new MaterialDialog(a)
                .title(R.string.popup_title_location, null)
                .message(R.string.popup_message_location, null, false, 1f)
                .cancelable(false)
                .positiveButton(R.string.ok_caps, null, d -> {
                    Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(enableLocationIntent, LOCATION_REQUEST);
                    locationDialog = null;
                    return Unit.INSTANCE;
                });

            Timber.d("Location not enabled");
        }
    };

    private void updateTitleText() {
        int count = leAdapter.getCount();
        String title = getString(R.string.connect_list_title);
        if(count > 0) {
            title += "  (" + count + ")";
        }
        connectPage.setListTitle(title);
    }

    private final LeScannerListener leScannerListener = new LeScannerListener() {

        @Override
        public void addDevice(BluetoothDevice device) {
            handler.removeCallbacks(locationChecker);
            runOnUiThread(() -> {
                leAdapter.addDevice(device);
                if(locationDialog != null) {
                    locationDialog.dismiss();
                    locationDialog = null;
                }
                updateTitleText();
            });
        }

        @Override
        public void clear() {
            leAdapter.clear();
            updateTitleText();
        }
    };

    private void connectLeDevice(BluetoothDevice device) {
        leScanner.cancelDiscovery();
        leClient.connectLeDevice(device);
    }

    private void showBluetoothRequiredDialog() {
        MaterialDialog dialog = new MaterialDialog(this)
            .title(R.string.popup_title_bluetooth_error, null)
            .message(R.string.popup_message_bluetooth_error, null, false, 0)
            .positiveButton(R.string.ok_caps, null, null);
        DialogCallbackExtKt.onDismiss(dialog, d -> {
            finish();
            return Unit.INSTANCE;
        });
        dialog.show();
    }

    @Override
    public void servicesDiscovered() {
        startActivity(new Intent(this, MainActivity.class));
    }

}

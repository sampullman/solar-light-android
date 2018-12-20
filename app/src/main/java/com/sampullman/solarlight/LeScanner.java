package com.sampullman.solarlight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.ParcelUuid;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import timber.log.Timber;

import static com.sampullman.solarlight.GattAttributes.SOLAR_UUID;

public class LeScanner {
    private static final long SCAN_PERIOD = 7000;

    public enum ScanState {
        OFF, BLUETOOTH_OFF, LE_UNAVAILABLE, ON
    }

    private final BluetoothAdapter adapter;

    // for API >= Lollipop(21)
    private BluetoothLeScanner leScanner;
    private ScanCallback scanCallback;

    private ScanState state = ScanState.OFF;
    private final ArrayList<BluetoothDevice> devices = new ArrayList<>();

    private Set<String> savedDevices;
    private final Handler handler = new Handler();
    private final List<ScanFilter> filters = new ArrayList<>();
    private ScanSettings settings;
    private final LeScannerListener listener;

    interface LeScannerListener {
        void addDevice(BluetoothDevice device);
        void clear();
    }

    LeScanner(@NonNull LeScannerListener listener) {
        this(listener, new HashSet<>());
    }

    LeScanner(@NonNull LeScannerListener listener, Set<String> savedDevices) {
        this.listener = listener;
        adapter = BluetoothAdapter.getDefaultAdapter();
        this.savedDevices = savedDevices;
        getLeScanner();
    }

    private boolean getLeScanner() {
        if (leScanner == null && Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            scanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, final ScanResult result) {
                    addDevice(result.getDevice());
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    for (final ScanResult sr : results) {
                        addDevice(sr.getDevice());
                    }
                }

                @Override
                public void onScanFailed(int errorCode) {
                    Timber.e("Scan Failed, Error Code: %d", errorCode);
                }
            };

            leScanner = adapter.getBluetoothLeScanner();
            settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
            // TODO -- add back in once UUID is updated
            // ParcelUuid uuid = new ParcelUuid(SOLAR_UUID);
            // filters.add(new ScanFilter.Builder().setServiceUuid(uuid).build());

        } else if(Build.VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {

        }
        return leScanner != null;
    }

    private LeScanCallback oldScanCallback = (BluetoothDevice d, int i, byte[] bytes) ->
        addDevice(d);

    void setSavedDevices(Set<String> savedDevices) {
        this.savedDevices = savedDevices;
    }

    private final Runnable scanRunnable = () -> {
        boolean continueScanning = (state != ScanState.OFF);
        stopScan();
        if(continueScanning) {
            startDiscovery();
            Timber.i("BLE Scan refresh");
        } else {
            Timber.i("BLE Scan timeout");
        }
    };

    public boolean isScanning() {
        return state == ScanState.ON;
    }

    ScanState startDiscovery() {
        // Stops scanning after a pre-defined scan period.
        handler.postDelayed(scanRunnable, SCAN_PERIOD);
        return startScan();
    }

    private ScanState startScan() {
        if(adapter.getState() == BluetoothAdapter.STATE_ON) {

            if(Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                if(getLeScanner()) {
                    state = ScanState.ON;
                    leScanner.startScan(filters, settings, scanCallback);
                } else {
                    state = ScanState.LE_UNAVAILABLE;
                }
            } else {
                adapter.startLeScan(oldScanCallback);
                state = ScanState.ON;
            }
        } else {
            state = ScanState.BLUETOOTH_OFF;
        }
        return state;
    }

    private void stopScan() {
        if(isScanning() && adapter.isEnabled()) {
            state = ScanState.OFF;
            handler.removeCallbacks(scanRunnable);
            if(Build.VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
                adapter.stopLeScan(oldScanCallback);

            } else if(getLeScanner()) {
                leScanner.stopScan(scanCallback);
            }
        }
    }

    void cancelDiscovery() {
        Timber.d("Canceled discovery");
        stopScan();
        clear();
    }

    private void clear() {
        devices.clear();
        listener.clear();
    }

    private void addDevice(BluetoothDevice device) {
        if(device.getName() == null || "".equals(device.getName())) {
            return;
        }
        String address = device.getAddress();
        String name = device.getName();

        if(!savedDevices.contains(address) && !devices.contains(device)) {
            Timber.d("Added device. name: %s, address: %s", name, address);
            devices.add(device);
            listener.addDevice(device);
        }
    }
}
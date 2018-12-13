package com.sampullman.solarlight;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.sampullman.ble.BluetoothLeService;
import com.sampullman.ble.LeClient;
import com.sampullman.ble.LeConnection;
import com.sampullman.ble.event.LeConnectionEvent;
import com.sampullman.ble.event.LeEvent;
import com.sampullman.ble.event.LeReadEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import timber.log.Timber;

public class SolarLeClient extends LeClient {
    private SolarLeClientListener listener;

    public interface SolarLeClientListener {
        void onEvent(LeEvent event);
        void servicesDiscovered();
    }

    public SolarLeClient(Context appContext) {
        super(appContext);
        setBleListener(bleListener);
    }

    public void registerListener(SolarLeClientListener listener) {
        this.listener = listener;
    }

    public void unregisterListener(SolarLeClientListener listener) {
        if(this.listener == listener) {
            this.listener = null;
        }
    }

    private void handleEvent(LeEvent event) {
        if(listener != null) {
            listener.onEvent(event);
        }
    }

    private final BleListener bleListener = new BleListener() {

        public void servicesDiscovered(LeConnection connection) {
            if(listener != null) {
                listener.servicesDiscovered();
            }
        }

        public void characteristicRead(LeConnection connection, String uuid, byte[] data) {

            if(data != null && connection != null) {
                handleEvent(new LeReadEvent(connection, uuid, data));
            } else if(connection == null) {
                Timber.e("Read characteristic connection null");
            } else {
                Timber.e("Read characteristic data null: %s", uuid);
            }
        }

        public void characteristicNotification(LeConnection connection, String uuid, byte[] data) {

            if(data == null || data.length < 2) {
                Timber.d("Notify characteristic data null or len<2, null=%b!", data == null);

            }
            Timber.d("Unhandled handleNotifyCharacteristic");
        }

        public void characetersticWriteComplete(LeConnection connection, String uuid, byte[] data) {
            // SolarLeConnection solarConnection = (SolarLeConnection)connection;
            if(data == null) {
                Timber.d("Characteristic written, no data");
                return;
            }
        }
    };

    public void connectLeDevice(BluetoothDevice device) {
        BluetoothLeService leService = getBleService();
        Timber.e("Connecting the LE device");
        if(leService == null) {
            Timber.e("BLE Service not available");
        } else {
            SolarLeConnection connection = new SolarLeConnection(leService, device);
            connectLeDelayed(connection);
        }
    }

    public boolean isActiveDevice(BluetoothDevice device) {
        SolarLeConnection connection = (SolarLeConnection)getConnectionFromDevice(device);
        return (connection != null) && connection.isActive();
    }

    // Send a BluetoothMessage to all LE connections that don't have an active Classic connection
    // Send Classic message using BluetoothController if leOnly is false
    public void broadcast(int cmd) {
        for(LeConnection c : getConnections()) {
            SolarLeConnection connection = (SolarLeConnection)c;

            String name = connection.getDevice().getName();
            Timber.d("Broadcast %02X to %s, active=%b", cmd, name, connection.isActive());
            connection.sendLeCmd(cmd);
        }
    }

    public void setActiveConnection(LeConnection connection) {
        for(LeConnection c : getConnections()) {
            SolarLeConnection pc = (SolarLeConnection)c;
            pc.setActive(false);
        }

        SolarLeConnection podoConnection = (SolarLeConnection)connection;
        podoConnection.setActive(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LeConnectionEvent event) {
        Timber.d("Received the LeConnectionEvent error %b", event.isError());
        SolarLeConnection conn = (SolarLeConnection)event.getLeConnection();
    }
}

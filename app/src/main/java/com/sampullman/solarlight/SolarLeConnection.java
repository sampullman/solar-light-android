package com.sampullman.solarlight;

import android.bluetooth.BluetoothDevice;

import com.sampullman.ble.BluetoothLeService;
import com.sampullman.ble.LeConnection;

import java.util.UUID;

import static com.sampullman.solarlight.GattAttributes.SOLAR_BATTERY_UUID;
import static com.sampullman.solarlight.GattAttributes.SOLAR_CONTROL_UUID;
import static com.sampullman.solarlight.GattAttributes.SOLAR_LED_CONTROL_UUID;
import static com.sampullman.solarlight.GattAttributes.SOLAR_UUID;

public class SolarLeConnection extends LeConnection {
    private boolean active = false;
    private boolean verified = false;
    private boolean checkedVerify = false;

    public SolarLeConnection(BluetoothLeService leService, BluetoothDevice device) {
        super(leService, device);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public void servicesDiscovered() {
        super.servicesDiscovered();

        requestNotification(SOLAR_BATTERY_UUID, true);
    }

    @Override
    public void disconnected() {
        super.disconnected();
        setVerified(false);
    }

    public boolean isVerified() {
        return verified;
    }

    public boolean hasCheckedVerify() {
        return checkedVerify;
    }

    public void setVerified(boolean verified) {
        this.checkedVerify = true;
        this.verified = verified;
    }


    public void requestNotification(UUID uuid, boolean on) {
        requestNotification(SOLAR_UUID, uuid, on);
    }

    public void readCharacteristic(UUID characteristic) {
        readCharacteristic(SOLAR_UUID, characteristic);
    }

    public void writeCharacteristic(UUID characteristic, byte[] data) {
        writeCharacteristic(SOLAR_UUID, characteristic, data);
    }

    // sends LE command at the front of send queue
    public void sendLedCmd(int cmd) {
        sendLedCmd(cmd, 0);
    }

    private void sendLedCmd(int cmd, int arg) {
        byte[] msg = new byte[] { (byte)cmd, (byte)arg };

        writeCharacteristic(SOLAR_LED_CONTROL_UUID, msg);
    }

    public void sendLeCmd(int cmd) {
        sendLeCmd(cmd, null);
    }

    private void sendLeCmd(int cmd, byte[] data) {
        byte[] buffer;
        if(data == null) {
            buffer = new byte[1];
        } else {
            buffer = new byte[1 + data.length];
            System.arraycopy(data, 0, buffer, 1, data.length);
        }
        buffer[0] = (byte)cmd;
        writeCharacteristic(SOLAR_CONTROL_UUID, buffer);
    }
}

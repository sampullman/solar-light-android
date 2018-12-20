package com.sampullman.solarlight;

import java.util.UUID;

public class GattAttributes {

    private static final String SOLAR_UUID_STRING = "D2DF677B-8b81-11e5-a837-0800200c9a66";
    public static final UUID SOLAR_UUID = UUID.fromString(SOLAR_UUID_STRING);

    private static final String SOLAR_NAME_UUID_STRING = "00002a00-0000-1000-8000-00805f9b34fb";
    public static UUID SOLAR_NAME_UUID = UUID.fromString(SOLAR_NAME_UUID_STRING);

    private static final String SOLAR_ADC1_UUID_STRING = "15005991-b131-3396-014c-664c9867b917";
    public static final UUID SOLAR_ADC1_UUID = UUID.fromString(SOLAR_ADC1_UUID_STRING);

    private static final String SOLAR_CONTROL_UUID_STRING = "f0f01001-8b81-11e5-a837-0800200c9a66";
    public static final UUID SOLAR_CONTROL_UUID = UUID.fromString(SOLAR_CONTROL_UUID_STRING);

    private static final String SOLAR_LED_CONTROL_UUID_STRING = "f0f01001-8b81-11e5-a837-0800200c9a66";
    public static final UUID SOLAR_LED_CONTROL_UUID = UUID.fromString(SOLAR_LED_CONTROL_UUID_STRING);

    private static final String SOLAR_BATTERY_UUID_STRING = "6c290d2e-1c03-aca1-ab48-a9b908bae79e";
    public static final UUID SOLAR_BATTERY_UUID = UUID.fromString(SOLAR_BATTERY_UUID_STRING);

}

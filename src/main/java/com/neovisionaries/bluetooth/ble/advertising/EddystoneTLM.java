/*
 * Copyright (C) 2015 Neo Visionaries Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neovisionaries.bluetooth.ble.advertising;


import com.neovisionaries.bluetooth.ble.util.Bytes;


/**
 * Eddystone TLM.
 *
 * @since 1.5
 *
 * @see <a href="https://github.com/google/eddystone/tree/master/eddystone-tlm">Eddystone TLM</a>
 */
public class EddystoneTLM extends Eddystone
{
    private static final long serialVersionUID = 1L;
    private static final String STRING_FORMAT = "EddystoneTLM(Version=%d,BatteryVoltage=%d,BeaconTemperature=%f,AdvertisementCount=%d,ElapsedTime=%d)";


    private final int mTLMVersion;
    private final int mBatteryVoltage;
    private final float mBeaconTemperature;
    private final long mAdvertisementCount;
    private final long mElapsedTime;
    private transient String mString;


    /**
     * Constructor to create an instance with length=17, type=0x16
     * (Service Data - 16-bit UUID), service-UUID=0xFEAA (Eddystone),
     * frame-type=TLM, tlm-version=0, battery-voltage=0,
     * beacon-temperature=-128, advertisement-count=0, and elapsed-time=0.
     */
    public EddystoneTLM()
    {
        this(17, 0x16, new byte[] {
                (byte)0xAA, (byte)0xFE,                         // Service UUID of Eddystone.
                (byte)0x20,                                     // Frame Type = Eddystone TLM
                (byte)0x00,                                     // TLM version (0)
                (byte)0x00, (byte)0x00,                         // Battery voltage (0)
                (byte)0x80, (byte)0x00,                         // Beacon temperature (-128)
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, // Advertisement count (0)
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00  // Elapsed time (0)
        });
    }


    /**
     * Constructor.
     *
     * @param length
     *         The length of the AD structure.
     *
     * @param type
     *         The AD type. The value should always be 0x16
     *         (Service Data - 16-bit UUID).
     *
     * @param data
     *         The AD data whose format is <i>Eddystone TLM</i>.
     *         The first two bytes should be 0xAA and 0xFE
     *         (meaning Eddystone). The third byte should be 0x2?
     *         (meaning Eddystone TLM).
     */
    public EddystoneTLM(int length, int type, byte[] data)
    {
        super(length, type, data, FrameType.TLM);

        mTLMVersion         = extractTLMVersion(data);
        mBatteryVoltage     = extractBatteryVoltage(data);
        mBeaconTemperature  = extractBeaconTemperature(data);
        mAdvertisementCount = extractAdvertisementCount(data);
        mElapsedTime        = extractElapsedTime(data);
    }


    private int extractTLMVersion(byte[] data)
    {
        // data[0] = 0xAA  // Eddystone
        // data[1] = 0xFE  //
        // data[2] = 0x2?  // Frame Type = Eddystone TLM
        // data[3]         // TLM version

        if (data.length < 4)
        {
            return 0;
        }

        return (data[3] & 0xFF);
    }


    private int extractBatteryVoltage(byte[] data)
    {
        if (data.length < 6)
        {
            return 0;
        }

        return Bytes.parseBE2BytesAsInt(data, 4);
    }


    private float extractBeaconTemperature(byte[] data)
    {
        if (data.length < 8)
        {
            return -128.0F;
        }

        // The temperature is expressed in a signed fixed-point notation.
        return Bytes.convertFixedPointToFloat(data, 6);
    }


    private long extractAdvertisementCount(byte[] data)
    {
        if (data.length < 12)
        {
            return 0;
        }

        return Bytes.parseBE4BytesAsUnsigned(data, 8);
    }


    private long extractElapsedTime(byte[] data)
    {
        if (data.length < 16)
        {
            return 0;
        }

        long time = Bytes.parseBE4BytesAsUnsigned(data, 12);

        // From 0.1 second resolution to millisecond resolution.
        time *= 100;

        return time;
    }


    /**
     * Get the TLM version.
     *
     * @return
     *         The TLM version.
     */
    public int getTLMVersion()
    {
        return mTLMVersion;
    }


    /**
     * Get the current battery charge in millivolts, expressed as 1 mV per bit.
     * If not supported (for example in a USB-powered beacon) the value is zero.
     *
     * @return
     *         The current battery charge in millivolts, expressed as 1 mV per bit.
     */
    public int getBatteryVoltage()
    {
        return mBatteryVoltage;
    }


    /**
     * Get the beacon temperature in degrees Celsius sensed by the beacon.
     * If not supported, the value is -128.
     *
     * @return
     *         The beacon temperature in degrees Celsius.
     */
    public float getBeaconTemperature()
    {
        return mBeaconTemperature;
    }


    /**
     * Get the count of advertisement frames of all types emitted by the beacon
     * since power-up or reboot.
     *
     * @return
     *         The count of advertisement frames.
     */
    public long getAdvertisementCount()
    {
        return mAdvertisementCount;
    }


    /**
     * Get the elapsed time in milliseconds since beacon power-up or reboot.
     *
     * @return
     *         The elapsed time in milliseconds.
     */
    public long getElapsedTime()
    {
        return mElapsedTime;
    }


    @Override
    public String toString()
    {
        if (mString != null)
        {
            return mString;
        }

        mString = String.format(STRING_FORMAT,
                mTLMVersion, mBatteryVoltage, mBeaconTemperature, mAdvertisementCount, mElapsedTime);

        return mString;
    }
}

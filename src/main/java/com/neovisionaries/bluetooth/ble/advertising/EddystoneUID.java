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
 * Eddystone UID.
 *
 * @since 1.5
 *
 * @see <a href="https://github.com/google/eddystone/tree/master/eddystone-uid">Eddystone UID</a>
 */
public class EddystoneUID extends Eddystone
{
    private static final long serialVersionUID = 1L;
    private static final String STRING_FORMAT = "EddyStoneUID(TxPower=%d,NamespaceId=%s,InstanceId=%s)";


    private final int mTxPower;
    private transient byte[] mNamespaceId;
    private transient byte[] mInstanceId;
    private transient byte[] mBeaconId;
    private transient String mNamespaceIdAsString;
    private transient String mInstanceIdAsString;
    private transient String mBeaconIdAsString;
    private transient String mString;


    /**
     * Constructor to create an instance with length=23, type=0x16
     * (Service Data - 16-bit UUID), service-UUID=0xFEAA (Eddystone),
     * frame-type=UID, tx-power=0, namespace-id=0, instance-id=0,
     * and reserved=0.
     */
    public EddystoneUID()
    {
        this(23, 0x16, new byte[] {
                (byte)0xAA, (byte)0xFE,                         // Service UUID of Eddystone.
                (byte)0x00,                                     // Frame Type = Eddystone UID
                (byte)0x00,                                     // Calibrated Tx power at 0 m
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, // 10-byte Namespace ID
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, // 6-byte Instance ID
                (byte)0x00, (byte)0x00,
                (byte)0x00, (byte)0x00                          // Reserved
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
     *         The AD data whose format is <i>Eddystone UID</i>.
     *         The first two bytes should be 0xAA and 0xFE
     *         (meaning Eddystone). The third byte should be 0x0?
     *         (meaning Eddystone UID).
     */
    public EddystoneUID(int length, int type, byte[] data)
    {
        super(length, type, data, FrameType.UID);

        mTxPower = extractTxPower(data);
    }



    private int extractTxPower(byte[] data)
    {
        // data[0] = 0xAA  // Eddystone
        // data[1] = 0xFE  //
        // data[2] = 0x0?  // Frame Type = Eddystone UID
        // data[3]         // Calibrated Tx power at 0 m.

        if (4 <= data.length)
        {
            return data[3];
        }
        else
        {
            return 0;
        }
    }


    /**
     * Get the calibrated Tx power at 0 m.
     *
     * @return
     *         The calibrated Tx power at 0 m.
     */
    public int getTxPower()
    {
        return mTxPower;
    }


    /**
     * Get the 10-byte namespace ID.
     *
     * @return
     *         The namesapce ID.
     */
    public byte[] getNamespaceId()
    {
        if (mNamespaceId == null)
        {
            mNamespaceId = Bytes.copyOfRange(getData(), 4, 14);
        }

        return mNamespaceId;
    }


    /**
     * Get the 10-byte namespace ID as an upper-case hex string.
     *
     * @return
     *         The namespace ID.
     */
    public String getNamespaceIdAsString()
    {
        if (mNamespaceIdAsString == null)
        {
            mNamespaceIdAsString = Bytes.toHexString(getNamespaceId(), true);
        }

        return mNamespaceIdAsString;
    }


    /**
     * Get the 6-byte instance ID.
     *
     * @return
     *         The instance ID.
     */
    public byte[] getInstanceId()
    {
        if (mInstanceId == null)
        {
            mInstanceId = Bytes.copyOfRange(getData(), 14, 20);
        }

        return mInstanceId;
    }


    /**
     * Get the 6-byte instance ID as an upper-case hex string.
     *
     * @return
     *         The instance ID.
     */
    public String getInstanceIdAsString()
    {
        if (mInstanceIdAsString == null)
        {
            mInstanceIdAsString = Bytes.toHexString(getInstanceId(), true);
        }

        return mInstanceIdAsString;
    }


    /**
     * Get the 16-byte beacon ID.
     *
     * <blockquote>
     * <pre>Beacon ID = Namespace ID + Instance ID</pre>
     * </blockquote>
     *
     * @return
     *         The beacon ID.
     */
    public byte[] getBeaconId()
    {
        if (mBeaconId == null)
        {
            mBeaconId = Bytes.copyOfRange(getData(), 4, 20);
        }

        return mBeaconId;
    }


    /**
     * Get the 16-byte beacon ID as an upper-case hex string.
     *
     * @return
     *         The beacon ID.
     */
    public String getBeaconIdAsString()
    {
        if (mBeaconIdAsString == null)
        {
            mBeaconIdAsString = Bytes.toHexString(getBeaconId(), true);
        }

        return mBeaconIdAsString;
    }


    @Override
    public String toString()
    {
        if (mString != null)
        {
            return mString;
        }

        mString = String.format(STRING_FORMAT,
                mTxPower, getNamespaceIdAsString(), getInstanceIdAsString());

        return mString;
    }
}

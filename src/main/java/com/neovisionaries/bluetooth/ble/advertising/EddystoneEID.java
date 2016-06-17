/*
 * Copyright (C) 2016 Neo Visionaries Inc.
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
 * @since 1.8
 *
 * @see <a href="https://github.com/google/eddystone/tree/master/eddystone-eid">Eddystone EID</a>
 */
public class EddystoneEID extends Eddystone
{
    private static final long serialVersionUID = 1L;
    private static final String STRING_FORMAT = "EddyStoneEID(TxPower=%d,EID=%s)";


    private final int mTxPower;
    private transient byte[] mEID;
    private transient String mEIDAsString;
    private transient String mString;


    /**
     * Constructor to create an instance with length=13, type=0x16
     * (Service Data - 16-bit UUID), service-UUID=0xFEAA (Eddystone),
     * frame-type=EID, tx-power=0 and eid=0.
     */
    public EddystoneEID()
    {
        this(13, 0x16, new byte[] {
                (byte)0xAA, (byte)0xFE,                         // Service UUID of Eddystone.
                (byte)0x30,                                     // Frame Type = Eddystone EID
                (byte)0x00,                                     // Calibrated Tx power at 0 m
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, // 8-byte EID
                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
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
     *         The AD data whose format is <i>Eddystone EID</i>.
     *         The first two bytes should be 0xAA and 0xFE
     *         (meaning Eddystone). The third byte should be 0x3?
     *         (meaning Eddystone EID).
     */
    public EddystoneEID(int length, int type, byte[] data)
    {
        super(length, type, data, FrameType.EID);

        mTxPower = extractTxPower(data);
    }


    private int extractTxPower(byte[] data)
    {
        // data[0] = 0xAA  // Eddystone
        // data[1] = 0xFE  //
        // data[2] = 0x3?  // Frame Type = Eddystone EID
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
     * Get the 8-byte EID (ephemeral identifier).
     *
     * @return
     *         The EID (ephemeral identifier).
     */
    public byte[] getEID()
    {
        if (mEID == null)
        {
            mEID = Bytes.copyOfRange(getData(), 4, 12);
        }

        return mEID;
    }


    /**
     * Get the 8-byte EID (ephemeral identifier) as an upper-case hex string.
     *
     * @return
     *         The EID (ephemeral identifier).
     */
    public String getEIDAsString()
    {
        if (mEIDAsString == null)
        {
            mEIDAsString = Bytes.toHexString(getEID(), true);
        }

        return mEIDAsString;
    }


    @Override
    public String toString()
    {
        if (mString == null)
        {
            mString = String.format(STRING_FORMAT, mTxPower, getEIDAsString());
        }

        return mString;
    }
}

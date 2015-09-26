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


import java.util.UUID;
import com.neovisionaries.bluetooth.ble.util.Bytes;
import com.neovisionaries.bluetooth.ble.util.UUIDCreator;


/**
 * iBeacon.
 */
public class IBeacon extends ADManufacturerSpecific
{
    private static final long serialVersionUID = 2L;
    private static final int UUID_INDEX  =  4;
    private static final int MAJOR_INDEX = 20;
    private static final int MINOR_INDEX = 22;
    private static final int POWER_INDEX = 24;
    private static final String STRING_FORMAT =
        "iBeacon(UUID=%s,Major=%d,Minor=%d,Power=%d)";

    private UUID mUUID;
    private int mMajor;
    private int mMinor;
    private int mPower;


    /**
     * Constructor which creates an instance with UUID=all-zeros,
     * major=0, minor=0, and power=0.
     */
    public IBeacon()
    {
        this(26, 0xFF, new byte[] {
            (byte)0x4C, (byte)0x00,   // Apple, Inc.
            (byte)0x02, (byte)0x15,   // iBeacon

            // Proximity UUID
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,

            (byte)0x00, (byte)0x00,   // Major number
            (byte)0x00, (byte)0x00,   // Minor number
            (byte)0x00                // Power
        }, 0x004C);
    }


    /**
     * Constructor.
     *
     * @param length
     *         The length of the AD structure.
     *
     * @param type
     *         The AD type. The value should always be 0xFF
     *         (Manufacturer Specific Data).
     *
     * @param data
     *         The AD data whose format is <i>iBeacon</i>.
     *
     * @param companyId
     *         The company ID. The value should always be 0x004C
     *         (Apple, Inc.).
     *
     * @throws IllegalArgumentException
     *         {@code data} is {@code null} or the length of
     *         {@code data} is less than 25.
     */
    public IBeacon(int length, int type, byte[] data, int companyId)
    {
        super(length, type, data, companyId);

        parse(data);
    }


    /**
     * Get the proximity UUID.
     *
     * @return
     *         The proximity UUID.
     */
    public UUID getUUID()
    {
        return mUUID;
    }


    /**
     * Set the proximity UUID.
     *
     *
     * @param uuid
     *         The proximity UUID. The value must not be {@code null}.
     *
     * @throws IllegalArgumentException
     *         The given value is {@code null}.
     */
    public void setUUID(UUID uuid)
    {
        if (uuid == null)
        {
            throw new IllegalArgumentException("'uuid' is null.");
        }

        mUUID = uuid;

        long msbits = uuid.getMostSignificantBits();
        long lsbits = uuid.getLeastSignificantBits();

        byte[] data = getData();
        data[UUID_INDEX +  0] = (byte)((msbits >> 56) & 0xFF);
        data[UUID_INDEX +  1] = (byte)((msbits >> 48) & 0xFF);
        data[UUID_INDEX +  2] = (byte)((msbits >> 40) & 0xFF);
        data[UUID_INDEX +  3] = (byte)((msbits >> 32) & 0xFF);
        data[UUID_INDEX +  4] = (byte)((msbits >> 24) & 0xFF);
        data[UUID_INDEX +  5] = (byte)((msbits >> 16) & 0xFF);
        data[UUID_INDEX +  6] = (byte)((msbits >>  8) & 0xFF);
        data[UUID_INDEX +  7] = (byte)((msbits      ) & 0xFF);
        data[UUID_INDEX +  8] = (byte)((lsbits >> 56) & 0xFF);
        data[UUID_INDEX +  9] = (byte)((lsbits >> 48) & 0xFF);
        data[UUID_INDEX + 10] = (byte)((lsbits >> 40) & 0xFF);
        data[UUID_INDEX + 11] = (byte)((lsbits >> 32) & 0xFF);
        data[UUID_INDEX + 12] = (byte)((lsbits >> 24) & 0xFF);
        data[UUID_INDEX + 13] = (byte)((lsbits >> 16) & 0xFF);
        data[UUID_INDEX + 14] = (byte)((lsbits >>  8) & 0xFF);
        data[UUID_INDEX + 15] = (byte)((lsbits >>  0) & 0xFF);
    }


    /**
     * Get the major number.
     *
     * @return
     *         The major number.
     */
    public int getMajor()
    {
        return mMajor;
    }


    /**
     * Set the major number.
     *
     * @param major
     *         The major number. The value should be in the range from 0 to 65535.
     *
     * @throws IllegalArgumentException
     *         The given value is out of the valid range.
     */
    public void setMajor(int major)
    {
        if (major < 0 || 0xFFFF < major)
        {
            throw new IllegalArgumentException("'major' is out of the valid range: " + major);
        }

        mMajor = major;

        byte[] data = getData();
        data[MAJOR_INDEX    ] = (byte)((major >> 8) & 0xFF);
        data[MAJOR_INDEX + 1] = (byte)((major     ) & 0xFF);
    }


    /**
     * Get the minor number.
     *
     * @return
     *         The minor number.
     */
    public int getMinor()
    {
        return mMinor;
    }


    /**
     * Set the minor number.
     *
     * @param minor
     *         The minor number. The value should be in the range from 0 to 65535.
     *
     * @throws IllegalArgumentException
     *         The given value is out of the valid range.
     */
    public void setMinor(int minor)
    {
        if (minor < 0 || 0xFFFF < minor)
        {
            throw new IllegalArgumentException("'minor' is out of the valid range: " + minor);
        }

        mMinor = minor;

        byte[] data = getData();
        data[MINOR_INDEX    ] = (byte)((minor >> 8) & 0xFF);
        data[MINOR_INDEX + 1] = (byte)((minor     ) & 0xFF);
    }


    /**
     * Get the power.
     *
     * @return
     *         The power.
     */
    public int getPower()
    {
        return mPower;
    }


    /**
     * Set the power.
     *
     * @param power
     *         The power. The value should be in the range from -128 to 127.
     *
     * @throws IllegalArgumentException
     *         The given value is out of the valid range.
     */
    public void setPower(int power)
    {
        if (power < -128 || 127 < power)
        {
            throw new IllegalArgumentException("'power' is out of the valid range: " + power);
        }

        mPower = power;

        getData()[POWER_INDEX] = (byte)(power & 0xFF);
    }


    private void parse(byte[] data)
    {
        // 25 = 2 (company ID) + 2 (format ID) + 16 (UUID) + 2 (major) + 2 (minor) + 1 (power)
        if (data == null || data.length < 25)
        {
            throw new IllegalArgumentException("The byte sequence cannot be parsed as an iBeacon.");
        }

        mUUID  = buildUUID(data);
        mMajor = buildMajor(data);
        mMinor = buildMinor(data);
        mPower = buildPower(data);
    }


    private UUID buildUUID(byte[] data)
    {
        return UUIDCreator.from128(data, 4, false);
    }


    private int buildMajor(byte[] data)
    {
        return Bytes.parseBE2BytesAsInt(data, MAJOR_INDEX);
    }


    private int buildMinor(byte[] data)
    {
        return Bytes.parseBE2BytesAsInt(data, MINOR_INDEX);
    }


    private int buildPower(byte[] data)
    {
        return data[POWER_INDEX];
    }


    /**
     * Create an {@link IBeacon} instance.
     *
     * <p>
     * The format of {@code data} should be as described in the following table.
     * </p>
     *
     * <table border="1" style="border-collapse: collapse;" cellpadding="5">
     *   <thead>
     *     <tr>
     *       <th></th>
     *       <th>Value</th>
     *       <th>Description</th>
     *     </tr>
     *   </thead>
     *   <tbody>
     *     <tr>
     *       <td>Company ID</td>
     *       <td><code>0x4C 0x00</code></td>
     *       <td>The company ID assigned to Apple, Inc. (Little Endian)</td>
     *     </tr>
     *     <tr>
     *       <td>Format ID</td>
     *       <td><code>0x02 0x15</code></td>
     *       <td>The format ID which represents <i>iBeacon</i></td>
     *     </tr>
     *     <tr>
     *       <td>Proximity UUID</td>
     *       <td>16-byte data</td>
     *       <td>Proximity UUID</td>
     *     </tr>
     *     <tr>
     *       <td>Major number</td>
     *       <td>2-byte data</td>
     *       <td>Major number (Big Endian)</td>
     *     </tr>
     *     <tr>
     *       <td>Minor number</td>
     *       <td>2-byte data</td>
     *       <td>Minor number (Big Endian)</td>
     *     </tr>
     *     <tr>
     *       <td>Power</td>
     *       <td>1-byte data</td>
     *       <td>The 2's complement of the calibrated Tx Power</td>
     *     </tr>
     *   </tbody>
     * </table>
     *
     * @param length
     *         The length of the AD structure.
     *
     * @param type
     *         The AD type. The value should always be 0xFF which represents
     *         <i>Manufacturer Specific Data</i>.
     *
     * @param data
     *         The AD type. The value of the first two bytes is the company ID.
     *
     * @param companyId
     *         The company ID. The value should always be 0x004C which represents
     *         <i>Apple, Inc.</i>
     *
     * @return
     *         An {@link IBeacon} instance. {@code null} is returned if the
     *         length of {@code data} is less than 25.
     */
    public static IBeacon create(int length, int type, byte[] data, int companyId)
    {
        if (data == null || data.length < 25)
        {
            return null;
        }

        return new IBeacon(length, type, data, companyId);
    }


    @Override
    public String toString()
    {
        return String.format(STRING_FORMAT, mUUID, mMajor, mMinor, mPower);
    }
}

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
import com.neovisionaries.bluetooth.ble.util.UUIDCreator;


/**
 * iBeacon.
 */
public class IBeacon extends ADManufacturerSpecific
{
    private static final long serialVersionUID = 1L;
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
     *         The proximity UUID.
     */
    public void setUUID(UUID uuid)
    {
        mUUID = uuid;
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
     */
    public void setMajor(int major)
    {
        mMajor = major;
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
     */
    public void setMinor(int minor)
    {
        mMinor = minor;
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
     *         The power.
     */
    public void setPower(int power)
    {
        mPower = power;
    }


    private void parse(byte[] data)
    {
        // 25 = 2 (company ID) + 2 (format ID) + 16 (UUID) + 2 (major) + 2 (minor) + 1 (power)
        if (data.length < 25)
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
        return UUIDCreator.from128(data, 4);
    }


    private int parseBE2BytesAsInt(byte[] data, int offset)
    {
        int value = ((data[offset + 0] & 0xFF) << 8)
                  | ((data[offset + 1] & 0xFF) << 0);

        return value;
    }


    private int buildMajor(byte[] data)
    {
        return parseBE2BytesAsInt(data, 20);
    }


    private int buildMinor(byte[] data)
    {
        return parseBE2BytesAsInt(data, 22);
    }


    private int buildPower(byte[] data)
    {
        return data[24];
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

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


import java.util.regex.Pattern;


/**
 * ucode: 128-bit ID for real objects.
 *
 * <p>
 * ucode (written in lower case) is a global ID system defined by
 * <a href="http://www.t-engine.org/">T-Engine Forum</a>.
 * The specification has been adopted as ITU-T H.642.
 * </p>
 *
 * <p>
 * This class represents <i>Bluetooth LE ucode Marker</i> which is
 * an AD structure to convey a ucode in BLE advertising packets.
 * </p>
 *
 * @since 1.1
 *
 * @see <a href="http://www.uidcenter.org/ja/wp-content/themes/wp.vicuna/pdf/UID-00049-01.A0.01.pdf"
 *      >Packet Format Specification for Bluetooth LE ucode Marker [01.A0.01] (in Japanese)</a>
 * @see <a href="http://en.wikipedia.org/wiki/Ucode_system"
 *      >Ucode system</a>
 */
public class Ucode extends ADManufacturerSpecific
{
    private static final long serialVersionUID = 1L;
    private static final int VERSION_INDEX = 2;
    private static final int UCODE_INDEX   = 3;
    private static final int STATUS_INDEX  = 19;
    private static final int POWER_INDEX   = 20;
    private static final int COUNT_INDEX   = 21;
    private static final int LOW_BATTERY_BIT = 0x20;
    private static final String UCODE_FORMAT
        = "%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X";
    private static final String STRING_FORMAT
        = "ucode(Version=%d,Ucode=%s,Status=%d,BatteryLow=%s,Interval=%d,Power=%d,Count=%d)";
    private static final Pattern UCODE_PATTERN = Pattern.compile("^[0-9A-Fa-f]{32}$");

    private int mVersion;
    private String mUcode;
    private int mStatus;
    private int mPower;
    private int mCount;


    /**
     * Constructor which creates an instance with version=3,
     * ucode=all-zeros, status=0, power=0, count=0.
     */
    public Ucode()
    {
        this(26, 0xFF, new byte[] {
            (byte)0x9A, (byte)0x01,   // T-Engine Forum (0x019A)
            (byte)0x03,               // Version (3)

            // Ucode
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,

            (byte)0x00,   // Status
            (byte)0x00,   // Power
            (byte)0x00,   // Count

            // Reserved
            (byte)0x00, (byte)0x00, (byte)0x00
            }, 0x019A);
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
     *         The AD data whose format is <i>Bluetooth LE ucode Marker</i>.
     *
     * @param companyId
     *         The company ID. The value should always be 0x019A
     *         (T-Engine Forum).
     *
     * @throws IllegalArgumentException
     *         {@code data} is {@code null} or the length of
     *         {@code data} is less than 22.
     */
    public Ucode(int length, int type, byte[] data, int companyId)
    {
        super(length, type, data, companyId);

        parse(data);
    }


    /**
     * Get the version of the packet format of Bluetooth LE ucode Marker.
     *
     * @return
     *         The version number.
     */
    public int getVersion()
    {
        return mVersion;
    }


    /**
     * Set the version of the packet format of Bluetooth LE ucode Marker.
     *
     * @param version
     *         The version number. The value should be in the range
     *         from 0 to 255.
     *
     * @throws IllegalArgumentException
     *         The given value is out of the valid range.
     */
    public void setVersion(int version)
    {
        if (version < 0 || 255 < version)
        {
            throw new IllegalArgumentException("'version' is out of the valid range: " + version);
        }

        mVersion = version;

        getData()[VERSION_INDEX] = (byte)(version & 0xFF);
    }


    /**
     * Get the ucode.
     *
     * @return
     *         The string representation of the ucode. It consists of
     *         32 upper-case hex letters.
     */
    public String getUcode()
    {
        return mUcode;
    }


    /**
     * Set the ucode.
     *
     * @param ucode
     *         The string representation of the ucode. It must consist
     *         of 32 hex letters.
     *
     * @throws IllegalArgumentException
     *         {@code ucode} is {@code null} or it does not consist of
     *         32 hex letters.
     */
    public void setUcode(String ucode)
    {
        if (ucode == null)
        {
            throw new IllegalArgumentException("'ucode' is null.");
        }

        if (UCODE_PATTERN.matcher(ucode).matches() == false)
        {
            throw new IllegalArgumentException("The format of 'ucode' is wrong: " + ucode);
        }

        mUcode = ucode;

        byte[] data = getData();

        for (int i = 0; i < 32; i += 2)
        {
            // Parse the two hex letters.
            byte value = readHex(ucode, i);

            // Store the value in the little endian order.
            int offset = UCODE_INDEX + (15 - i / 2);

            data[offset] = value;
        }
    }


    private byte readHex(String string, int index)
    {
        int i0 = toInt(string.charAt(index));
        int i1 = toInt(string.charAt(index + 1));

        return (byte)(((i0 << 4) | i1) & 0xFF);
    }


    private int toInt(char ch)
    {
        if ('0' <= ch && ch <= '9')
        {
            return ch - '0';
        }
        else if ('A' <= ch && ch <= 'F')
        {
            return ch - 'A' + 10;
        }
        else if ('a' <= ch && ch <= 'f')
        {
            return ch - 'a' + 10;
        }
        else
        {
            // This should not happen.
            return 0;
        }
    }


    /**
     * Get the status.
     *
     * <p>
     * The meanings of the bits are as follows.
     * </p>
     *
     * <table border="1" cellpadding="5" style="border-collapse: collapse;">
     *   <thead>
     *     <tr align="center">
     *       <td width="60">7</td>
     *       <td width="60">6</td>
     *       <td width="60">5</td>
     *       <td width="60">4</td>
     *       <td width="60">3</td>
     *       <td width="60">2</td>
     *       <td width="60">1</td>
     *       <td width="60">0</td>
     *     </tr>
     *   </thead>
     *   <tbody>
     *     <tr align="center">
     *       <td>Reserved</td>
     *       <td>Reserved</td>
     *       <td>Low<br/>Battery</td>
     *       <td>1</td>
     *       <td colspan="4">
     *         Transmission Interval
     *       </td>
     *   </tbody>
     * </table>
     *
     * <p>
     * Combinations of the bits of Transmission Interval have the following
     * meanings.
     * </p>
     *
     * <table border="1" cellpadding="5" style="border-collapse: collapse;">
     *   <thead>
     *     <tr>
     *       <th>Bits</th>
     *       <th>Interval (ms)</th>
     *     </tr>
     *   </thead>
     *   <tbody>
     *     <tr>
     *       <td><code>0b0000</code></td>
     *       <td align="right">10</td>
     *     </tr>
     *     <tr>
     *       <td><code>0b0001</code></td>
     *       <td align="right">20</td>
     *     </tr>
     *     <tr>
     *       <td><code>0b0010</code></td>
     *       <td align="right">40</td>
     *     </tr>
     *     <tr>
     *       <td><code>0b0011</code></td>
     *       <td align="right">80</td>
     *     </tr>
     *     <tr>
     *       <td><code>0b0100</code></td>
     *       <td align="right">160</td>
     *     </tr>
     *     <tr>
     *       <td><code>0b0101</code></td>
     *       <td align="right">320</td>
     *     </tr>
     *     <tr>
     *       <td><code>0b0110</code></td>
     *       <td align="right">640</td>
     *     </tr>
     *     <tr>
     *       <td><code>0b0111</code></td>
     *       <td align="right">1280</td>
     *     </tr>
     *     <tr>
     *       <td><code>0b1000</code></td>
     *       <td align="right">2560</td>
     *     </tr>
     *     <tr>
     *       <td><code>0b1001</code></td>
     *       <td align="right">5120</td>
     *     </tr>
     *     <tr>
     *       <td>Others</td>
     *       <td align="right">10240</td>
     *     </tr>
     *   </tbody>
     * </table>
     *
     * @return
     *         The status.
     */
    public int getStatus()
    {
        return mStatus;
    }


    /**
     * Set the status.
     *
     * @param status
     *         The status.
     */
    public void setStatus(int status)
    {
        mStatus = status;

        getData()[STATUS_INDEX] = (byte)(status & 0xFF);
    }


    /**
     * Get the flag value that indicates whether the battery is low or not.
     *
     * <p>
     * The returned value is the boolean representation of the Low Battery
     * bit of the status.
     * </p>
     *
     * @return
     *         The flag value that indicates whether the battery is low or not.
     */
    public boolean isBatteryLow()
    {
        return ((mStatus & LOW_BATTERY_BIT) != 0);
    }


    /**
     * Get the transmission interval in milliseconds.
     *
     * <p>
     * The returned value is calculated from the bits of Transmission Interval
     * in the status.
     * </p>
     *
     * @return
     *         The transmission interval in milliseconds.
     */
    public int getInterval()
    {
        switch (mStatus & 0xF)
        {
            case 0x0: return    10;
            case 0x1: return    20;
            case 0x2: return    40;
            case 0x3: return    80;
            case 0x4: return   160;
            case 0x5: return   320;
            case 0x6: return   640;
            case 0x7: return  1280;
            case 0x8: return  2560;
            case 0x9: return  5120;
            default:  return 10240;
        }
    }


    /**
     * Get the transmission power in dBm.
     *
     * @return
     *         The transmission power in dBm.
     */
    public int getPower()
    {
        return mPower;
    }


    /**
     * Set the transmission power in dBm.
     *
     * @param power
     *         The transmission power in dBm. The valid range is from -128 to 127.
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

        getData()[POWER_INDEX] = (byte)power;
    }


    /**
     * Get the transmission count.
     *
     * <p>
     * The value of the transmission count field is incremented
     * per transmission. Note that the next value of 0xFF is 0x00.
     * </p>
     *
     * @return
     *         The transmission count.
     */
    public int getCount()
    {
        return mCount;
    }


    /**
     * Set the transmission count.
     *
     * @param count
     *         The transmission count. The value should be in
     *         the range from 0x00 to 0xFF.
     *
     * @throws IllegalArgumentException
     *         The given value is out of the valid range.
     */
    public void setCount(int count)
    {
        if (count < 0 || 0xFF < count)
        {
            throw new IllegalArgumentException("'count' is out of the valid range: " + count);
        }

        mCount = count;

        getData()[COUNT_INDEX] = (byte)count;
    }


    private void parse(byte[] data)
    {
        // 22 = 2 (company ID) + 1 (version) + 16 (ucode) + 1 (status) + 1 (power) + 1 (counter)
        //
        // The reserved three bytes at the end of ucode packets are ignored
        // in the current implementation.
        if (data == null || data.length < 22)
        {
            throw new IllegalArgumentException("The byte sequence cannot be parsed as a ucode.");
        }

        mVersion = buildVersion(data);
        mUcode   = buildUcode(data);
        mStatus  = buildStatus(data);
        mPower   = buildPower(data);
        mCount   = buildCount(data);
    }


    private int buildVersion(byte[] data)
    {
        return data[VERSION_INDEX] & 0xFF;
    }


    private String buildUcode(byte[] data)
    {
        // Ucode is packed in the little endian order.
        return String.format(UCODE_FORMAT,
            data[UCODE_INDEX + 15] & 0xFF, data[UCODE_INDEX + 14] & 0xFF,
            data[UCODE_INDEX + 13] & 0xFF, data[UCODE_INDEX + 12] & 0xFF,
            data[UCODE_INDEX + 11] & 0xFF, data[UCODE_INDEX + 10] & 0xFF,
            data[UCODE_INDEX +  9] & 0xFF, data[UCODE_INDEX +  8] & 0xFF,
            data[UCODE_INDEX +  7] & 0xFF, data[UCODE_INDEX +  6] & 0xFF,
            data[UCODE_INDEX +  5] & 0xFF, data[UCODE_INDEX +  4] & 0xFF,
            data[UCODE_INDEX +  3] & 0xFF, data[UCODE_INDEX +  2] & 0xFF,
            data[UCODE_INDEX +  1] & 0xFF, data[UCODE_INDEX +  0] & 0xFF);
    }


    private int buildStatus(byte[] data)
    {
        return data[STATUS_INDEX] & 0xFF;
    }


    private int buildPower(byte[] data)
    {
        // signed.
        return data[POWER_INDEX];
    }


    private int buildCount(byte[] data)
    {
        // unsigned.
        return data[COUNT_INDEX] & 0xFF;
    }


    /**
     * Create a {@link Ucode} instance.
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
     *       <td><code>0x9A 0x01</code></td>
     *       <td>The company ID assigned to T-Engine Forum. (Little Endian)</td>
     *     </tr>
     *     <tr>
     *       <td>Version</td>
     *       <td>1-byte unsigned number</td>
     *       <td>The version number of <i>Bluetooth LE ucode Marker</i>.</td>
     *     </tr>
     *     <tr>
     *       <td>Ucode</td>
     *       <td>16-byte data</td>
     *       <td>Ucode in the little endian order.</td>
     *     </tr>
     *     <tr>
     *       <td>Status</td>
     *       <td>1-byte data</td>
     *       <td>Bit flags that represents the status of the peripheral device.</td>
     *     </tr>
     *     <tr>
     *       <td>Transmission Power</td>
     *       <td>1-byte signed number.</td>
     *       <td>Transmission power in dBm.</td>
     *     </tr>
     *     <tr>
     *       <td>Transmission Count</td>
     *       <td>1-byte unsigned number.</td>
     *       <td>The counter value incremented per transmission.</td>
     *     </tr>
     *     <tr>
     *       <td>Reserved</td>
     *       <td>3-byte zeros.</td>
     *       <td>Reserved for future use.</td>
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
     *         The company ID. The value should always be 0x019A which represents
     *         <i>T-Engine Forum</i>
     *
     * @return
     *         A {@link Ucode} instance. {@code null} is returned if the
     *         length of {@code data} is less than 22.
     */
    public static Ucode create(int length, int type, byte[] data, int companyId)
    {
        if (data == null || data.length < 22)
        {
            return null;
        }

        return new Ucode(length, type, data, companyId);
    }


    @Override
    public String toString()
    {
        return String.format(STRING_FORMAT,
            mVersion, mUcode, mStatus, isBatteryLow(), getInterval(), mPower, mCount);
    }
}

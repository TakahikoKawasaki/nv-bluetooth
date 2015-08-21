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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Utility to parse a payload of an advertising packet. Especially,
 * the third argument of
 * <a href="http://developer.android.com/reference/android/bluetooth/BluetoothAdapter.LeScanCallback.html#onLeScan%28android.bluetooth.BluetoothDevice,%20int,%20byte[]%29"
 * >onLeScan</a> method of android.<wbr>bluetooth.<wbr>BluetoothAdapter.<wbr>LeScanCallback
 * interface.
 *
 * <pre style="padding: 0.5em; border: 1px solid black; margin: 1em;">
 * public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
 * {
 *     List&lt;ADStructure&gt; structures = ADPayloadParser.{@link #getInstance()}.{@link
 *     #parse(byte[]) parse}(scanRecord);
 * </pre>
 *
 * @author Takahiko Kawasaki
 */
public class ADPayloadParser
{
    // The singleton instance.
    private static final ADPayloadParser sInstance = new ADPayloadParser();


    private final Map<Integer, List<ADStructureBuilder>> mBuilders;
    private final Map<Integer, List<ADManufacturerSpecificBuilder>> mMSBuilders;


    private ADPayloadParser()
    {
        // Builders for Manufacturer Specific Data.
        mMSBuilders = new HashMap<Integer, List<ADManufacturerSpecificBuilder>>();
        registerManufacturerSpecificBuilder(0x004C, new MS004CBuilder());
        registerManufacturerSpecificBuilder(0x0105, new MS0105Builder());
        registerManufacturerSpecificBuilder(0x019A, new MS019ABuilder());

        // Builder for AD structures that list UUIDs.
        UUIDsBuilder uuidsBuilder = new UUIDsBuilder();

        // Builder for AD structures that contain a local name.
        LocalNameBuilder localNameBuilder = new LocalNameBuilder();

        // Builder for AD structures for Service Data.
        ServiceDataBuilder serviceDataBuilder = new ServiceDataBuilder();

        // Builders.
        mBuilders = new HashMap<Integer, List<ADStructureBuilder>>();

        // 0x01: Flags
        registerBuilder(0x01, new FlagsBuilder());

        // 0x02: Incomplete List of 16-bit Service Class UUIDs
        registerBuilder(0x02, uuidsBuilder);

        // 0x03: Complete List of 16-bit Service Class UUIDs
        registerBuilder(0x03, uuidsBuilder);

        // 0x04: Incomplete List of 32-bit Service Class UUIDs
        registerBuilder(0x04, uuidsBuilder);

        // 0x05: Complete List of 32-bit Service Class UUIDs
        registerBuilder(0x05, uuidsBuilder);

        // 0x06: Incomplete List of 128-bit Service Class UUIDs
        registerBuilder(0x06, uuidsBuilder);

        // 0x07: Complete List of 128-bit Service Class UUIDs
        registerBuilder(0x07, uuidsBuilder);

        // 0x08: Shortened Local Name
        registerBuilder(0x08, localNameBuilder);

        // 0x09: Complete Local Name
        registerBuilder(0x09, localNameBuilder);

        // 0x0A: Tx Power Level
        registerBuilder(0x0A, new TxPowerLevelBuilder());

        // 0x14: List of 16-bit Service Solicitation UUIDs
        registerBuilder(0x14, uuidsBuilder);

        // 0x15: List of 128-bit Service Solicitation UUIDs
        registerBuilder(0x15, uuidsBuilder);

        // 0x16: Service Data - 16-bit UUID
        registerBuilder(0x16, serviceDataBuilder);
        registerBuilder(0x16, new EddystoneBuilder());

        // 0x1F: List of 32-bit Service Solicitation UUIDs
        registerBuilder(0x1F, uuidsBuilder);

        // 0x20: Service Data - 32-bit UUID
        registerBuilder(0x20, serviceDataBuilder);

        // 0x21: Service Data - 128-bit UUID
        registerBuilder(0x21, serviceDataBuilder);

        // 0xFF: Manufacturer Specific Data
        registerBuilder(0xFF, new MSBuilder());
    }


    /**
     * Get the singleton instance.
     */
    public static ADPayloadParser getInstance()
    {
        return sInstance;
    }


    /**
     * Register an AD structure builder for the AD type. The given builder
     * is added at the beginning of the list of the builders for the AD type.
     *
     * <p>
     * Note that a builder for the type <i>Manufacturer Specific Data</i>
     * (0xFF) should not be registered by this method. Instead, use
     * {@link #registerManufacturerSpecificBuilder(int,
     * ADManufacturerSpecificBuilder)}.
     * </p>
     *
     * @param type
     *         AD type. The value must be in the range from 0 to 0xFF.
     *
     * @param builder
     *         AD structure builder.
     */
    public void registerBuilder(int type, ADStructureBuilder builder)
    {
        if (type < 0 || 0xFF < type)
        {
            String message = String.format("'type' is out of the valid range: %d", type);
            throw new IllegalArgumentException(message);
        }

        if (builder == null)
        {
            return;
        }

        // Use the AD type as the key for the builder.
        Integer key = Integer.valueOf(type);

        // Get the existing list of builders for the AD type.
        List<ADStructureBuilder> builders = mBuilders.get(key);

        // If no builder has been registered for the AD type yet.
        if (builders == null)
        {
            builders = new ArrayList<ADStructureBuilder>();
            mBuilders.put(key, builders);
        }

        // Register the builder at the beginning of the builder list.
        builders.add(0, builder);
    }


    /**
     * Register a builder for the company ID. The given builder is added
     * at the beginning of the list of the builders for the company ID.
     *
     * @param companyId
     *         Company ID. The value must be in the range from 0 to 0xFFFF.
     *
     * @param builder
     *         A builder.
     */
    public void registerManufacturerSpecificBuilder(int companyId, ADManufacturerSpecificBuilder builder)
    {
        if (companyId < 0 || 0xFFFF < companyId)
        {
            String message = String.format("'companyId' is out of the valid range: %d", companyId);
            throw new IllegalArgumentException(message);
        }

        if (builder == null)
        {
            return;
        }

        // Use the company ID as the key for the builder.
        Integer key = Integer.valueOf(companyId);

        // Get the existing list of builders for the company ID.
        List<ADManufacturerSpecificBuilder> builders = mMSBuilders.get(key);

        // If no builder has been registered for the company ID yet.
        if (builders == null)
        {
            builders = new ArrayList<ADManufacturerSpecificBuilder>();
            mMSBuilders.put(key, builders);
        }

        // Register the builder at the beginning of the builder list.
        builders.add(0, builder);
    }


    /**
     * Parse a byte sequence as a list of AD structures.
     *
     * @param payload
     *         A byte array containing of AD structures.
     *
     * @return
     *         A list of parsed AD structures. If {@code payload}
     *         is {@code null}, this method returns {@code null}.
     */
    public List<ADStructure> parse(byte[] payload)
    {
        if (payload == null)
        {
            return null;
        }

        return parse(payload, 0, payload.length);
    }


    /**
     * Parse a byte sequence as a list of AD structures.
     *
     * <p>
     * Supported AD structures are as follows.
     * </p>
     *
     * <blockquote>
     * <table border="1" cellpadding="5" style="border-collapse: collapse;">
     *   <thead>
     *     <tr>
     *       <th>Data Type Value</th>
     *       <th>Data Type Name</th>
     *       <th>Class</th>
     *     </tr>
     *   </thead>
     *   <tbody>
     *     <tr>
     *       <td><code>0x01</code></td>
     *       <td>Flags</td>
     *       <td>{@link Flags}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0x02</code></td>
     *       <td>Incomplete List of 16-bit Service Class UUIDs</td>
     *       <td>{@link UUIDs}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0x03</code></td>
     *       <td>Complete List of 16-bit Service Class UUIDs</td>
     *       <td>{@link UUIDs}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0x04</code></td>
     *       <td>Incomplete List of 32-bit Service Class UUIDs</td>
     *       <td>{@link UUIDs}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0x05</code></td>
     *       <td>Complete List of 32-bit Service Class UUIDs</td>
     *       <td>{@link UUIDs}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0x06</code></td>
     *       <td>Incomplete List of 128-bit Service Class UUIDs</td>
     *       <td>{@link UUIDs}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0x07</code></td>
     *       <td>Complete List of 128-bit Service Class UUIDs</td>
     *       <td>{@link UUIDs}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0x14</code></td>
     *       <td>List of 16-bit Service Solicitation UUIDs</td>
     *       <td>{@link UUIDs}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0x15</code></td>
     *       <td>List of 128-bit Service Solicitation UUIDs</td>
     *       <td>{@link UUIDs}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0x16</code></td>
     *       <td>Service Data - 16-bit UUID</td>
     *       <td>{@link ServiceData}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0x1F</code></td>
     *       <td>List of 32-bit Service Solicitation UUIDs</td>
     *       <td>{@link UUIDs}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0x20</code></td>
     *       <td>Service Data - 32-bit UUID</td>
     *       <td>{@link ServiceData}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0x21</code></td>
     *       <td>Service Data - 128-bit UUID</td>
     *       <td>{@link ServiceData}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0xFF</code></td>
     *       <td>Manufacturer Specific Data</td>
     *       <td>{@link ADManufacturerSpecific}</td>
     *     </tr>
     *   </tbody>
     * </table>
     * </blockquote>
     *
     * <p>
     * In addition, some specific Manufacturer Specific Data are supported.
     * </p>
     *
     * <blockquote>
     * <table border="1" cellpadding="5" style="border-collapse: collapse;">
     *   <thead>
     *     <tr>
     *       <th>Data Type Value</th>
     *       <th>Company ID</th>
     *       <th>Company Name</th>
     *       <th>Format</th>
     *       <th>Class</th>
     *     </tr>
     *   </thead>
     *   <tbody>
     *     <tr>
     *       <td><code>0xFF</code></td>
     *       <td><code>0x004C</code></td>
     *       <td>Apple, Inc.</td>
     *       <td>iBeacon</td>
     *       <td>{@link IBeacon}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0xFF</code></td>
     *       <td><code>0x0105</code></td>
     *       <td>Ubiquitous Computing Technology Corporation</td>
     *       <td>ucode</td>
     *       <td>{@link Ucode}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0xFF</code></td>
     *       <td><code>0x019A</code></td>
     *       <td>T-Engine Forum</td>
     *       <td>ucode</td>
     *       <td>{@link Ucode}</td>
     *     </tr>
     *   </tbody>
     * </table>
     * </blockquote>
     *
     * <p>
     * {@link #registerBuilder(int, ADStructureBuilder) registerBuilder} and
     * {@link #registerManufacturerSpecificBuilder(int, ADManufacturerSpecificBuilder)
     * registerManufacturerSpecificBuilder} can be used to register your
     * customized parsers for AD structures.
     * </p>
     *
     * <p>
     * {@link ServiceData} has subclasses. The table below lists the supported
     * data formats.
     * </p>
     *
     * <blockquote>
     * <table border="1" cellpadding="5" style="border-collapse: collapse;">
     *   <thead>
     *     <tr>
     *       <th>Service UUID</th>
     *       <th>Format</th>
     *       <th>Class</th>
     *     </tr>
     *   </thead>
     *   <tbody>
     *     <tr>
     *       <td><code>0xFEAA</code> (<a href="https://github.com/google/eddystone">Eddystone</a>)</td>
     *       <td><a href="https://github.com/google/eddystone/tree/master/eddystone-uid">Eddystone UID</a></td>
     *       <td>{@link EddystoneUID}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0xFEAA</code> (<a href="https://github.com/google/eddystone">Eddystone</a>)</td>
     *       <td><a href="https://github.com/google/eddystone/tree/master/eddystone-url">Eddystone URL</a></td>
     *       <td>{@link EddystoneURL}</td>
     *     </tr>
     *     <tr>
     *       <td><code>0xFEAA</code> (<a href="https://github.com/google/eddystone">Eddystone</a>)</td>
     *       <td><a href="https://github.com/google/eddystone/tree/master/eddystone-tlm">Eddystone TLM</a></td>
     *       <td>{@link EddystoneTLM}</td>
     *     </tr>
     *   </tbody>
     * </table>
     * </blockquote>
     *
     * @param payload
     *         A byte array containing of AD structures.
     *
     * @param offset
     *         The offset from which parsing should be started.
     *
     * @param length
     *         The length of bytes to be parsed.
     *
     * @return
     *         A list of parsed AD structures. If {@code payload}
     *         is {@code null}, this method returns {@code null}.
     */
    public List<ADStructure> parse(byte[] payload, int offset, int length)
    {
        if (payload == null)
        {
            return null;
        }

        // List of AD structures.
        List<ADStructure> list = new ArrayList<ADStructure>();

        // If parameters are wrong.
        if (offset < 0 || length < 0 || payload.length <= offset)
        {
            // Simply return an empty list without throwing an exception.
            return list;
        }

        // The index to terminate the valid range.
        int end = Math.min(offset + length, payload.length);

        for (int i = offset; i < end; )
        {
            // The first octet represents 'length'.
            int len = payload[i] & 0xFF;

            // If the length is zero.
            if (len == 0)
            {
                // An excerpt from "11 ADVERTISING AND SCAN RESPONSE DATA FORMAT"
                // in Bluetooth Core Specification 4.2.
                //
                //   If the Length field is set to zero, then the Data field
                //   has zero octets. This shall only occur to allow an early
                //   termination of the Advertising or Scan Response data.

                // No more AD structures.
                break;
            }

            // If the remaining octets are insufficient.
            if ((end - i - 1) < len)
            {
                // Cannot parse the octets any more.
                break;
            }

            // The second octet represents 'type'.
            int type = payload[i + 1] & 0xFF;

            // The AD data.
            byte[] data = Arrays.copyOfRange(payload, i + 2, i + len + 1);

            // Build an AD structure.
            ADStructure ads = buildAds(len, type, data);

            // Add the AD structure to the list.
            list.add(ads);

            // Jump to the first octet of the next AD structure.
            i += 1 + len;
        }

        // Return the list of AD structures.
        return list;
    }


    private ADStructure buildAds(int length, int type, byte[] data)
    {
        // Get the list of builders for the AD type.
        List<ADStructureBuilder> builders = mBuilders.get(Integer.valueOf(type));

        // If builders for the AD type does not exist.
        if (builders == null)
        {
            // Create a generic instance.
            return new ADStructure(length, type, data);
        }

        // Until one of the builders succeeds building an AD structure.
        for (ADStructureBuilder builder : builders)
        {
            // Let the builder build an AD structure.
            ADStructure structure = builder.build(length, type, data);

            // If the builder successfully created an instance.
            if (structure != null)
            {
                return structure;
            }
        }

        // Create a generic instance.
        return new ADStructure(length, type, data);
    }


    private class MSBuilder implements ADStructureBuilder
    {
        @Override
        public ADStructure build(int length, int type, byte[] data)
        {
            // The AD data must contain a company ID in the first two octets.
            if (data.length < 2)
            {
                return null;
            }

            // Extract the company ID (little endian).
            int companyId = ((data[1] & 0xFF) << 8) | (data[0] & 0xFF);

            // Get the list of builders for the company ID.
            List<ADManufacturerSpecificBuilder> builders = mMSBuilders.get(Integer.valueOf(companyId));

            // If builders for the company ID does not exist.
            if (builders == null)
            {
                // Create a generic instance.
                return new ADManufacturerSpecific(length, type, data, companyId);
            }

            // Until one of the builders succeeds building an AD structure.
            for (ADManufacturerSpecificBuilder builder : builders)
            {
                // Let the builder build an AD structure.
                ADManufacturerSpecific structure = builder.build(length, type, data, companyId);

                // If the builder successfully created an instance.
                if (structure != null)
                {
                    return structure;
                }
            }

            // Create a generic instance.
            return new ADManufacturerSpecific(length, type, data, companyId);
        }
    }
}

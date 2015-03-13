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

        // Builders.
        mBuilders = new HashMap<Integer, List<ADStructureBuilder>>();
        registerBuilder(0x01, new ADFlagsBuilder());
        registerBuilder(0xFF, new MSBuilder());
    }


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

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
package com.neovisionaries.bluetooth.ble.util;


import java.util.UUID;


/**
 * Utility to create UUID instances.
 *
 * @since 1.1
 */
public class UUIDCreator
{
    /**
     * Bluetooth Base UUID used to build a complete UUID from a 16-bit/32-bit UUID.
     */
    private static final String BASE_UUID_FORMAT =
        "%02x%02x%02x%02x-0000-1000-8000-00805f9b34fb";

    /**
     * Generic UUID format that can be parsed by UUID.fromString(String).
     */
    private static final String GENERIC_UUID_FORMAT =
        "%02x%02x%02x%02x-%02x%02x-%02x%02x-%02x%02x-%02x%02x%02x%02x%02x%02x";


    private UUIDCreator()
    {
    }


    /**
     * Create a UUID instance from 16-bit UUID data (little endian).
     *
     * <p>
     * This method is an alias of {@link #from16(byte[], int) from16(data, 0)}.
     * </p>
     *
     * @param data
     *         A byte array containing 16-bit UUID data.
     *
     * @return
     *         A UUID instance. {@code null} is returned when {@code data}
     *         is {@code null} or {@code offset} is not valid.
     */
    public static UUID from16(byte[] data)
    {
        return from16(data, 0);
    }


    /**
     * Create a UUID instance from 16-bit UUID data (little endian).
     *
     * <p>
     * This method is an alias of {@link #from16(byte[], int, boolean)
     * from16(data, offset, true)}.
     * </p>
     *
     * @param data
     *         A byte array containing 16-bit UUID data.
     *
     * @param offset
     *         The offset from which 16-bit UUID data should be read.
     *
     * @return
     *         A UUID instance. {@code null} is returned when {@code data}
     *         is {@code null} or {@code offset} is not valid.
     */
    public static UUID from16(byte[] data, int offset)
    {
        return from16(data, offset, true);
    }


    /**
     * Create a UUID instance from 16-bit UUID data.
     *
     * <pre style="padding: 0.5em; margin: 1em; border: 1px solid black;">
     * // Prepare a byte array containing 32-bit UUID data (little endian).
     * byte[] data = new byte[] { (byte)0xAB, (byte)0xCD };
     *
     * // Create a UUID instance from the byte array.
     * UUID uuid = UUIDCreator.{@link #from16(byte[], int, boolean) from16}(data, 0, true);
     *
     * // uuid represents 0000cdab-0000-1000-8000-00805f9b34fb.
     * </p>
     *
     * @param data
     *         A byte array containing 16-bit UUID data.
     *
     * @param offset
     *         The offset from which 16-bit UUID data should be read.
     *
     * @param littleEndian
     *         {@code true} if the 16-bit UUID data is stored in little endian.
     *
     * @return
     *         A UUID instance. {@code null} is returned when {@code data}
     *         is {@code null} or {@code offset} is not valid.
     */
    public static UUID from16(byte[] data, int offset, boolean littleEndian)
    {
        if (data == null || offset < 0 || data.length <= (offset + 1) || Integer.MAX_VALUE == offset)
        {
            return null;
        }

        int v2, v3;

        if (littleEndian)
        {
            v2 = data[offset + 1] & 0xFF;
            v3 = data[offset + 0] & 0xFF;
        }
        else
        {
            v2 = data[offset + 0] & 0xFF;
            v3 = data[offset + 1] & 0xFF;
        }

        return fromBase(0, 0, v2, v3);
    }


    /**
     * Create a UUID instance from 32-bit UUID data (little endian).
     *
     * <p>
     * This method is an alias of {@link #from32(byte[], int) from32(data, 0)}.
     * </p>
     *
     * @param data
     *         A byte array containing 32-bit UUID data.
     *
     * @return
     *         A UUID instance. {@code null} is returned when {@code data}
     *         is {@code null} or {@code offset} is not valid.
     */
    public static UUID from32(byte[] data)
    {
        return from32(data, 0);
    }


    /**
     * Create a UUID instance from 32-bit UUID data (little endian).
     *
     * <p>
     * This method is an alias of {@link #from32(byte[], int, boolean)
     * from32(data, offset, true)}.
     * </p>
     *
     * @param data
     *         A byte array containing 32-bit UUID data.
     *
     * @param offset
     *         The offset from which 32-bit UUID data should be read.
     *
     * @return
     *         A UUID instance. {@code null} is returned when {@code data}
     *         is {@code null} or {@code offset} is not valid.
     */
    public static UUID from32(byte[] data, int offset)
    {
        return from32(data, offset, true);
    }


    /**
     * Create a UUID instance from 32-bit UUID data.
     *
     * <pre style="padding: 0.5em; margin: 1em; border: 1px solid black;">
     * // Prepare a byte array containing 32-bit UUID data (little endian).
     * byte[] data = new byte[] { (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF };
     *
     * // Create a UUID instance from the byte array.
     * UUID uuid = UUIDCreator.{@link #from32(byte[], int, boolean) from32}(data, 0, true);
     *
     * // uuid represents efcdab89-0000-1000-8000-00805f9b34fb.
     * </p>
     *
     * @param data
     *         A byte array containing 32-bit UUID data.
     *
     * @param offset
     *         The offset from which 32-bit UUID data should be read.
     *
     * @param littleEndian
     *         {@code true} if the 32-bit UUID data is stored in little endian.
     *
     * @return
     *         A UUID instance. {@code null} is returned when {@code data}
     *         is {@code null} or {@code offset} is not valid.
     */
    public static UUID from32(byte[] data, int offset, boolean littleEndian)
    {
        if (data == null || offset < 0 || data.length <= (offset + 3) || (Integer.MAX_VALUE - 3) < offset)
        {
            return null;
        }

        int v0, v1, v2, v3;

        if (littleEndian)
        {
            v0 = data[offset + 3] & 0xFF;
            v1 = data[offset + 2] & 0xFF;
            v2 = data[offset + 1] & 0xFF;
            v3 = data[offset + 0] & 0xFF;
        }
        else
        {
            v0 = data[offset + 0] & 0xFF;
            v1 = data[offset + 1] & 0xFF;
            v2 = data[offset + 2] & 0xFF;
            v3 = data[offset + 3] & 0xFF;
        }

        return fromBase(v0, v1, v2, v3);
    }


    private static UUID fromBase(int v0, int v1, int v2, int v3)
    {
        return UUID.fromString(String.format(BASE_UUID_FORMAT, v0, v1, v2, v3));
    }


    /**
     * Create a UUID instance from 128-bit UUID data.
     *
     * <p>
     * This method is an alias of {@link #from128(byte[], int) from32(data, 0)}.
     * </p>
     *
     * @param data
     *         A byte array containing 128-bit UUID data.
     *
     * @return
     *         A UUID instance. {@code null} is returned when {@code data}
     *         is {@code null} or {@code offset} is not valid.
     */
    public static UUID from128(byte[] data)
    {
        return from128(data, 0);
    }


    /**
     * Create a UUID instance from 128-bit UUID data.
     *
     * <pre style="padding: 0.5em; margin: 1em; border: 1px solid black;">
     * // Prepare a byte array containing 128-bit UUID data (little endian).
     * byte[] data = new byte[] {
     *     (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67,
     *     (byte)0x89, (byte)0xAB, (byte)0xCD, (byte)0xEF,
     *     (byte)0xFE, (byte)0xDC, (byte)0xBA, (byte)0x98,
     *     (byte)0x76, (byte)0x54, (byte)0x32, (byte)0x10 };
     *
     * // Create a UUID instance from the byte array.
     * UUID uuid = UUIDCreator.{@link #from32(byte[], int, boolean) from32}(data, 0, true);
     *
     * // uuid represents 01234567-89ab-cdef-fedc-ba9876543210.
     * </p>
     *
     * @param data
     *         A byte array containing 128-bit UUID data.
     *
     * @param offset
     *         The offset from which 128-bit UUID data should be read.
     *
     * @return
     *         A UUID instance. {@code null} is returned when {@code data}
     *         is {@code null} or {@code offset} is not valid.
     */
    public static UUID from128(byte[] data, int offset)
    {
        if (data == null || offset < 0 || data.length <= (offset + 15) || (Integer.MAX_VALUE - 15) < offset)
        {
            return null;
        }

        String uuid = String.format(GENERIC_UUID_FORMAT,
                data[offset + 15] & 0xFF, data[offset + 14] & 0xFF,
                data[offset + 13] & 0xFF, data[offset + 12] & 0xFF,
                data[offset + 11] & 0xFF, data[offset + 10] & 0xFF,
                data[offset +  9] & 0xFF, data[offset +  8] & 0xFF,
                data[offset +  7] & 0xFF, data[offset +  6] & 0xFF,
                data[offset +  5] & 0xFF, data[offset +  4] & 0xFF,
                data[offset +  3] & 0xFF, data[offset +  2] & 0xFF,
                data[offset +  1] & 0xFF, data[offset +  0] & 0xFF);
        
        return UUID.fromString(uuid);
    }
}

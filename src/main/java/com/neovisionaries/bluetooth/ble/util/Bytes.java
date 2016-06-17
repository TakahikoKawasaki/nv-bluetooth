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


/**
 * Utility for byte arrays.
 *
 * @since 1.5
 */
public class Bytes
{
    private static final char[] UPPER_HEX_CHARS = "0123456789ABCDEF".toCharArray();
    private static final char[] LOWER_HEX_CHARS = "0123456789abcdef".toCharArray();


    private Bytes()
    {
    }


    /**
     * Parse 2 bytes in big endian byte order as an {@code int}.
     */
    public static int parseBE2BytesAsInt(byte[] data, int offset)
    {
        int value = ((data[offset + 0] & 0xFF) << 8)
                  | ((data[offset + 1] & 0xFF) << 0);

        return value;
    }


    /**
     * Parse 4 bytes in big endian byte order as an {@code int}.
     */
    public static int parseBE4BytesAsInt(byte[] data, int offset)
    {
        int value = ((data[offset + 0] & 0xFF) << 24)
                  | ((data[offset + 1] & 0xFF) << 16)
                  | ((data[offset + 2] & 0xFF) <<  8)
                  | ((data[offset + 3] & 0xFF));

        return value;
    }


    /**
     * Parse 4 bytes in big endian byte order as an unsigned integer.
     */
    public static long parseBE4BytesAsUnsigned(byte[] data, int offset)
    {
        long value = ((long)(data[offset + 0] & 0xFF) << 24)
                   | ((long)(data[offset + 1] & 0xFF) << 16)
                   | ((long)(data[offset + 2] & 0xFF) <<  8)
                   | ((long)(data[offset + 3] & 0xFF) <<  0);

        return value;
    }


    /**
     * Convert 2 bytes in fixed point format to {@code float}.
     *
     * @see <a href="https://courses.cit.cornell.edu/ee476/Math/"
     *      >Cornell University, Electrical Engineering 476,
     *      Fixed Point mathematical functions in GCC and assembler</a>
     */
    public static float convertFixedPointToFloat(byte[] data, int offset)
    {
        return (data[offset] + ((data[offset + 1] & 0xFF) / 256.0F));
    }


    /**
     * Convert a byte array to a hex string.
     *
     * @param data
     *         An input byte array.
     *
     * @param upper
     *         {@code true} to generate a upper-case hex string.
     *         {@code false} to generate a lower-case hex string.
     *
     * @return
     *         A hex string. if {@code data} is {@code null},
     *         {@code null} is returned.
     */
    public static String toHexString(byte[] data, boolean upper)
    {
        if (data == null)
        {
            return null;
        }

        char[] table = (upper ? UPPER_HEX_CHARS : LOWER_HEX_CHARS);
        char[] chars = new char[data.length * 2];

        for (int i = 0; i < data.length; ++i)
        {
            chars[i * 2    ] = table[ (data[i] & 0xF0) >> 4 ];
            chars[i * 2 + 1] = table[ (data[i] & 0x0F)      ];
        }

        return new String(chars);
    }


    /**
     * Copy a range of a given byte array.
     *
     * @param source
     *         A source byte array.
     *
     * @param from
     *         The start index of the range in the source byte array (inclusive).
     *
     * @param to
     *         The end index of the range in the source byte array (exclusive).
     *
     * @return
     *         A copied byte array. {@code null} is returned if (1) {@code source}
     *         is {@code null}, (2) {@code from} is negative, (3) {@code to} is
     *         negative, (4) the copy length ({@code to - from}) is negative, or
     *         (5) {@code from +} the copy length exceeds {@code source.length}.
     *
     * @since 1.8
     */
    public static byte[] copyOfRange(byte[] source, int from, int to)
    {
        if (source == null || from < 0 || to < 0)
        {
            return null;
        }

        int length = to - from;

        if (length < 0)
        {
            return null;
        }

        if (source.length < from + length)
        {
            return null;
        }

        byte[] destination = new byte[length];

        System.arraycopy(source, from, destination, 0, length);

        return destination;
    }
}

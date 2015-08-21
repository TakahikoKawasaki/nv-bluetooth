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


import java.net.MalformedURLException;
import java.net.URL;


/**
 * Eddystone URL.
 *
 * @since 1.5
 *
 * @see <a href="https://github.com/google/eddystone/tree/master/eddystone-url">Eddystone URL</a>
 */
public class EddystoneURL extends Eddystone
{
    private static final long serialVersionUID = 1L;
    private static final String STRING_FORMAT = "EddystoneURL(TxPower=%d,URL=%s)";

    private static final String[] SCHEME_PREFIXES = {
        "http://www.",      // 0
        "https://www.",     // 1
        "http://",          // 2
        "https://",         // 3
    };

    private static final String[] EXPANSION_CODES = {
        ".com/",            //  0, 0x00
        ".org/",            //  1, 0x01
        ".edu/",            //  2, 0x02
        ".net/",            //  3, 0x03
        ".info/",           //  4, 0x04
        ".biz/",            //  5, 0x05
        ".gov/",            //  6, 0x06
        ".com",             //  7, 0x07
        ".org",             //  8, 0x08
        ".edu",             //  9, 0x09
        ".net",             // 10, 0x0A
        ".info",            // 11, 0x0B
        ".biz",             // 12, 0x0C
        ".gov",             // 13, 0x0D
    };


    private final int mTxPower;
    private final URL mURL;


    /**
     * Constructor to create an instance with length=5, type=0x16
     * (Service Data - 16-bit UUID), service-UUID=0xFEAA (Eddystone),
     * frame-type=URL, tx-power=0, and URL=null.
     */
    public EddystoneURL()
    {
        this(5, 0x16, new byte[] {
                (byte)0xAA, (byte)0xFE, // Service UUID of Eddystone.
                (byte)0x10,             // Frame Type = Eddystone URL
                (byte)0x00              // Calibrated Tx power at 0 m
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
     *         (meaning Eddystone). The third byte should be 0x1?
     *         (meaning Eddystone URL).
     */
    public EddystoneURL(int length, int type, byte[] data)
    {
        super(length, type, data, FrameType.URL);

        mTxPower = extractTxPower(data);
        mURL     = extractURL(data);
    }


    private int extractTxPower(byte[] data)
    {
        // data[0] = 0xAA  // Eddystone
        // data[1] = 0xFE  //
        // data[2] = 0x1?  // Frame Type = Eddystone URL
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


    private URL extractURL(byte[] data)
    {
        StringBuilder builder = new StringBuilder();

        // URL Scheme Prefix
        String prefix = extractSchemePrefix(data);
        if (prefix != null)
        {
            builder.append(prefix);
        }

        for (int i = 5; i < data.length; ++i)
        {
            int ch = data[i];

            if (0 <= ch && ch < EXPANSION_CODES.length)
            {
                builder.append(EXPANSION_CODES[ch]);
            }
            else if (0x20 < ch && ch < 0x7F)
            {
                builder.append((char)ch);
            }
        }

        if (builder.length() == 0)
        {
            return null;
        }

        try
        {
            return new URL(builder.toString());
        }
        catch (MalformedURLException e)
        {
            return null;
        }
    }


    private String extractSchemePrefix(byte[] data)
    {
        // data[4] = URL Scheme Prefix

        if (data.length < 5)
        {
            return null;
        }

        int code = data[4];

        if (code < 0 || SCHEME_PREFIXES.length <= code)
        {
            return null;
        }

        return SCHEME_PREFIXES[code];
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
     * Get the URL.
     *
     * @return
     *         The URL.
     */
    public URL getURL()
    {
        return mURL;
    }


    @Override
    public String toString()
    {
        return String.format(STRING_FORMAT, mTxPower, mURL);
    }
}

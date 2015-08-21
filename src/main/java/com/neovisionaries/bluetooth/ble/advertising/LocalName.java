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


import java.io.UnsupportedEncodingException;


/**
 * An AD structure of type "Shortened Local Name" (type = 0x08)
 * and "Complete Local Name" (type = 0x09).
 *
 * @since 1.3
 */
public class LocalName extends ADStructure
{
    private static final long serialVersionUID = 1L;
    private static final int SHORTENED = 0x08;
    private static final int COMPLETE  = 0x09;
    private static final String STRING_FORMAT = "LocalName(%s,%s)";


    private String mLocalName;


    /**
     * Constructor to create an instance with length=1, type=0x09
     * (Complete Local Name), and data={@code null}.
     */
    public LocalName()
    {
        // Type 0x09 represents "Complete Local Name".
        this(1, COMPLETE, null);
    }


    /**
     * Constructor.
     *
     * @param length
     *         The length of the AD structure. The value should be
     *         {@code data.length + 1}.
     *
     * @param type
     *         The AD type. The value should be either 0x08
     *         (Shortened Local Name) or 0x09 (Complete Local Name).
     *
     * @param data
     *         The AD data.
     */
    public LocalName(int length, int type, byte[] data)
    {
        super(length, type, data);

        parse(data);
    }


    private void parse(byte[] data)
    {
        if (data == null || data.length < 1)
        {
            return;
        }

        try
        {
            mLocalName = new String(data, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            // This never happens.
        }
    }


    /**
     * Check if this AD Structure represents "Shortened Local Name".
     *
     * <p>
     * This method returns {@code true} when {@link #getType()} returns 0x08.
     * </p>
     *
     * @return
     *         {@code true} if this AD structure represents
     *         "Shortened Local Name".
     */
    public boolean isShortened()
    {
        return (getType() == SHORTENED);
    }


    /**
     * Check if this AD Structure represents "Complete Local Name".
     *
     * <p>
     * This method returns {@code true} when {@link #getType()} returns 0x09.
     * </p>
     *
     * @return
     *         {@code true} if this AD structure represents
     *         "Complete Local Name".
     */
    public boolean isComplete()
    {
        return (getType() == COMPLETE);
    }


    /**
     * Get the local name.
     *
     * @return
     *         The local name.
     */
    public String getLocalName()
    {
        return mLocalName;
    }


    @Override
    public String toString()
    {
        return String.format(STRING_FORMAT,
            isShortened() ? "SHORTENED" : "COMPLETE", mLocalName);
    }
}

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


/**
 * An AD structure of type "Tx Power Level" (AD type = 0x0A).
 *
 * @since 1.3
 */
public class TxPowerLevel extends ADStructure
{
    private static final long serialVersionUID = 1L;
    private static final String STRING_FORMAT = "TxPowerLevel(%s%ddBm)";


    /**
     * Constructor to create an instance with length=2, type=0x0A,
     * and data={0x00}.
     */
    public TxPowerLevel()
    {
        // Type 0x0A represents "Tx Power Level".
        this(2, 0x0A, new byte[]{ (byte)0x00 });
    }


    /**
     * Constructor.
     *
     * @param length
     *         The length of the AD structure. The value should be
     *         {@code data.length + 1}.
     *
     * @param type
     *         The AD type. The value should always be 0x0A (Tx Power Level).
     *
     * @param data
     *         The AD data.
     */
    public TxPowerLevel(int length, int type, byte[] data)
    {
        super(length, type, data);
    }


    /**
     * Get the transmitted power level.
     *
     * @return
     *         The transmitted power level. From -127 to +127 dBm.
     */
    public int getLevel()
    {
        byte[] data = getData();

        if (data == null || data.length == 0)
        {
            return 0;
        }

        return data[0];
    }


    @Override
    public String toString()
    {
        int level = getLevel();

        return String.format(STRING_FORMAT, (0 <= level) ? "+" : "", level);
    }
}

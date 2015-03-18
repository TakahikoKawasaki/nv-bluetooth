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
 * An AD structure of type <i>Flags</i> (0x01).
 *
 * <p>
 * The format of the AD data is described in <i>"1.3 FLAGS"</i>
 * in <i>"Core Specification Supplement v5"</i>.
 * </p>
 *
 * @see <a href="https://www.bluetooth.org/en-us/specification/adopted-specifications"
 *      >Specification Adopted Documents</a>
 */
public class ADFlags extends ADStructure
{
    private static final long serialVersionUID = 1L;
    private static final String STRING_FORMAT =
        "Flags(LimitedDiscoverable=%s,GeneralDiscoverable=%s," +
        "LegacySupported=%s,ControllerSimultaneitySupported=%s,HostSimultaneitySupported=%s)";
    private boolean mLimitedDiscoverable;
    private boolean mGeneralDiscoverable;
    private boolean mLegacySupported;
    private boolean mControllerSimultaneitySupported;
    private boolean mHostSimultaneitySupported;


    /**
     * Constructor to create an instance with length=2, type=0x01,
     * and data={0x00}.
     */
    public ADFlags()
    {
        // Type 0x01 represents "Flags".
        this(2, 0x01, new byte[]{ (byte)0x00 });
    }


    /**
     * Constructor.
     *
     * @param length
     *         The length of the AD structure. The value should be
     *         {@code data.length + 1}.
     *
     * @param type
     *         The AD type. The value should always be 0x01 (Flags).
     *
     * @param data
     *         The AD data.
     */
    public ADFlags(int length, int type, byte[] data)
    {
        super(length, type, data);

        parse(data);
    }


    /**
     * Get the value of "LE Limited Discoverable Mode".
     */
    public boolean isLimitedDiscoverable()
    {
        return mLimitedDiscoverable;
    }


    /**
     * Set the value of "LE Limited Discoverable Mode".
     */
    public void setLimitedDiscoverable(boolean discoverable)
    {
        mLimitedDiscoverable = discoverable;
    }


    /**
     * Get the value of "LE General Discoverable Mode".
     */
    public boolean isGeneralDiscoverable()
    {
        return mGeneralDiscoverable;
    }


    /**
     * Set the value of "LE General Discoverable Mode".
     */
    public void setGeneralDiscoverable(boolean discoverable)
    {
        mGeneralDiscoverable = discoverable;
    }


    /**
     * Get the <i>inverted</i> value of "BR/EDR Not Supported".
     *
     * @return
     *         {@code true} when the bit of "BR/EDR Not Supported" is 0.
     */
    public boolean isLegacySupported()
    {
        return mLegacySupported;
    }


    /**
     * Set the <i>inverted</i> value of "BR/EDR Not Supported".
     */
    public void setLegacySupported(boolean supported)
    {
        mLegacySupported = supported;
    }


    /**
     * Get the value of "Simultaneous LE and BR/EDR to Same Device
     * Capable (Controller)".
     */
    public boolean isControllerSimultaneitySupported()
    {
        return mControllerSimultaneitySupported;
    }


    /**
     * Set the value of "Simultaneous LE and BR/EDR to Same Device
     * Capable (Controller)".
     */
    public void setControllerSimultaneitySupported(boolean supported)
    {
        mControllerSimultaneitySupported = supported;
    }


    /**
     * Get the value of "Simultaneous LE and BR/EDR to Same Device
     * Capable (Host)".
     */
    public boolean isHostSimultaneitySupported()
    {
        return mHostSimultaneitySupported;
    }


    /**
     * Set the value of "Simultaneous LE and BR/EDR to Same Device
     * Capable (Host)".
     */
    public void setHostSimultaneitySupported(boolean supported)
    {
        mHostSimultaneitySupported = supported;
    }


    private void parse(byte[] data)
    {
        if (data == null || data.length < 1)
        {
            return;
        }

        mLimitedDiscoverable             = (data[0] & 0x01) != 0;
        mGeneralDiscoverable             = (data[0] & 0x02) != 0;
        mLegacySupported                 = (data[0] & 0x04) == 0; // inverted
        mControllerSimultaneitySupported = (data[0] & 0x08) != 0;
        mHostSimultaneitySupported       = (data[0] & 0x10) != 0;
    }


    @Override
    public String toString()
    {
        return String.format(STRING_FORMAT,
            mLimitedDiscoverable, mGeneralDiscoverable, mLegacySupported,
            mControllerSimultaneitySupported, mHostSimultaneitySupported);
    }
}

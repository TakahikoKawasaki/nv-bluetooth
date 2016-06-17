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
 * Eddystone.
 *
 * @since 1.5
 *
 * @see <a href="https://github.com/google/eddystone">Eddystone</a>
 */
public abstract class Eddystone extends ServiceData
{
    /**
     * Eddystone frame types.
     *
     * @since 1.5
     */
    public enum FrameType
    {
        /**
         * <a href="https://github.com/google/eddystone/tree/master/eddystone-uid">Eddystone-UID</a>.
         */
        UID,

        /**
         * <a href="https://github.com/google/eddystone/tree/master/eddystone-url">Eddystone-URL</a>.
         */
        URL,

        /**
         * <a href="https://github.com/google/eddystone/tree/master/eddystone-tlm">Eddystone-TLM</a>.
         */
        TLM,

        /**
         * <a href="https://github.com/google/eddystone/tree/master/eddystone-eid">Eddystone-EID</a>.
         *
         * @since 1.8
         */
        EID
    }


    private static final long serialVersionUID = 1L;
    private static final String STRING_FORMAT = "EddyStone(FrameType=%s)";


    private final FrameType mFrameType;


    /**
     * Constructor to create an instance with length=3, type=0x16
     * (Service Data - 16-bit UUID), and service-UUID=0xFEAA
     * (Eddystone).
     */
    public Eddystone()
    {
        this(3, 0x16, new byte[] {
                (byte)0xAA, (byte)0xFE  // Service UUID of Eddystone.
        }, null);
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
     *         The AD data whose format is <i>Eddystone</i>.
     *         The first two bytes should be 0xAA and 0xFE.
     *
     * @param frameType
     *         The {@link FrameType frame type}.
     */
    public Eddystone(int length, int type, byte[] data, FrameType frameType)
    {
        super(length, type, data);

        mFrameType = frameType;
    }


    /**
     * Get the {@link FrameType frame type}.
     *
     * @return
     *         The frame type.
     */
    public FrameType getFrameType()
    {
        return mFrameType;
    }


    @Override
    public String toString()
    {
        return String.format(STRING_FORMAT, mFrameType);
    }
}

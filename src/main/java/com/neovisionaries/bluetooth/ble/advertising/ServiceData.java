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


import java.util.UUID;
import com.neovisionaries.bluetooth.ble.util.UUIDCreator;


/**
 * An AD structure for the following types (Service Data).
 *
 * <ul>
 *   <li>Service Data -  16-bit UUID (type = 0x16)
 *   <li>Service Data -  32-bit UUID (type = 0x20)
 *   <li>Service Data - 128-bit UUID (type = 0x21)
 * </ul>
 *
 * @since 1.5
 *
 * @see <a href="https://www.bluetooth.org/en-us/specification/adopted-specifications"
 *      >Supplement to the Bluetooth Core Specification Version 5, Part A, 1.11 Service Data</a>
 * @see <a href="https://www.bluetooth.org/en-us/specification/adopted-specifications"
 *      >Specification Adopted Documents (bluetooth.org)</a>
 */
public class ServiceData extends ADStructure
{
    private static final long serialVersionUID = 1L;
    private static final String STRING_FORMAT = "ServiceData(ServiceUUID=%s)";


    private final UUID mServiceUUID;


    /**
     * Constructor to create an instance with length=1, type=0x16
     * (Service Data - 16-bit UUID), and data={@code null}.
     */
    public ServiceData()
    {
        this(1, 0x16, null);
    }


    /**
     * Constructor.
     *
     * @param length
     *         The length of the AD structure. The value should be
     *         {@code data.length + 1}.
     *
     * @param type
     *         The AD type. The value should be one of
     *         0x16 (Service Data -  16-bit UUID),
     *         0x20 (Service Data -  32-bit UUID), and
     *         0x21 (Service Data - 128-bit UUID).
     *
     * @param data
     *         The AD data.
     */
    public ServiceData(int length, int type, byte[] data)
    {
        super(length, type, data);

        mServiceUUID = extractServiceUUID(type, data);
    }


    private UUID extractServiceUUID(int type, byte[] data)
    {
        switch (type)
        {
            // 16-bit UUID
            case 0x16:
                return UUIDCreator.from16(data);

            // 32-bit UUID
            case 0x20:
                return UUIDCreator.from32(data);

            // 128-bit UUID
            case 0x21:
                return UUIDCreator.from128(data);

            default:
                return null;
        }
    }


    /**
     * Get the service UUID.
     *
     * @return
     *         The service UUID.
     */
    public UUID getServiceUUID()
    {
        return mServiceUUID;
    }


    @Override
    public String toString()
    {
        return String.format(STRING_FORMAT, mServiceUUID);
    }
}

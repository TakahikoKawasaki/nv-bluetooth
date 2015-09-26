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


/**
 * An AD structure that contains a list of UUIDs.
 *
 * <p>
 * This class covers the following data types. See
 * <a href="https://www.bluetooth.org/en-us/specification/assigned-numbers/generic-access-profile"
 * >Generic Access Profile (GAP)</a> for the official list of data types.
 * </p>
 *
 * <table border="1" cellpadding="5" style="border-collapse: collapse;">
 *   <thead>
 *     <tr>
 *       <th>Data Type Value</th>
 *       <th>Data Type Name</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td><code>0x02</code></td>
 *       <td>Incomplete List of 16-bit Service Class UUIDs</td>
 *     </tr>
 *     <tr>
 *       <td><code>0x03</code></td>
 *       <td>Complete List of 16-bit Service Class UUIDs</td>
 *     </tr>
 *     <tr>
 *       <td><code>0x04</code></td>
 *       <td>Incomplete List of 32-bit Service Class UUIDs</td>
 *     </tr>
 *     <tr>
 *       <td><code>0x05</code></td>
 *       <td>Complete List of 32-bit Service Class UUIDs</td>
 *     </tr>
 *     <tr>
 *       <td><code>0x06</code></td>
 *       <td>Incomplete List of 128-bit Service Class UUIDs</td>
 *     </tr>
 *     <tr>
 *       <td><code>0x07</code></td>
 *       <td>Complete List of 128-bit Service Class UUIDs</td>
 *     </tr>
 *     <tr>
 *       <td><code>0x14</code></td>
 *       <td>List of 16-bit Service Solicitation UUIDs</td>
 *     </tr>
 *     <tr>
 *       <td><code>0x15</code></td>
 *       <td>List of 128-bit Service Solicitation UUIDs</td>
 *     </tr>
 *     <tr>
 *       <td><code>0x1F</code></td>
 *       <td>List of 32-bit Service Solicitation UUIDs</td>
 *     </tr>
 *   </tbody>
 * </table>
 *
 * <p>
 * This class was renamed from {@code ADUUIDs} to {@code UUIDs}
 * when the version was upgraded to 1.4.
 * </p>
 *
 * @since 1.1
 *
 * @see <a href="https://www.bluetooth.org/en-us/specification/assigned-numbers/generic-access-profile"
 *      >Generic Access Profile (GAP)</a>
 */
public class UUIDs extends ADStructure
{
    private static final long serialVersionUID = 1L;
    private static final String STRING_FORMAT = "UUIDs(%s)";

    private UUID[] mUUIDs;


    /**
     * Constructor to create an instance with length=0, type=0, data=null and UUIDs=null.
     */
    public UUIDs()
    {
    }


    /**
     * Constructor.
     *
     * @param length
     *         The length of the AD structure.
     *
     * @param type
     *         The AD type.
     *
     * @param data
     *         The AD data.
     *
     * @param uuids
     *         UUID list.
     */
    public UUIDs(int length, int type, byte[] data, UUID... uuids)
    {
        super(length, type, data);

        mUUIDs = uuids;
    }


    /**
     * Get the list of UUIDs.
     *
     * @return
     *         UUID list.
     */
    public UUID[] getUUIDs()
    {
        return mUUIDs;
    }


    /**
     * Set the list of UUIDs.
     *
     * @param uuids
     *         UUID list.
     */
    public void setUUIDs(UUID[] uuids)
    {
        mUUIDs = uuids;
    }


    @Override
    public String toString()
    {
        if (mUUIDs == null)
        {
            return String.format(STRING_FORMAT, "null");
        }

        // Build a comma-separated UUID list.
        StringBuilder builder = new StringBuilder();

        for (UUID uuid : mUUIDs)
        {
            builder.append(uuid).append(",");
        }

        if (builder.length() != 0)
        {
            // Remove the last ','.
            builder.setLength(builder.length() - 1);
        }

        return String.format(STRING_FORMAT, builder.toString());
    }
}

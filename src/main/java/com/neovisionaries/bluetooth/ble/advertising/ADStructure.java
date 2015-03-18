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


import java.io.Serializable;


/**
 * AD structure.
 *
 * <p>
 * This class represents an AD structure. The format of an AD structure
 * is described in <i>"11 ADVERTISING AND SCAN RESPONSE DATA FORMAT"</i>
 * in <i>"Bluetooth Core Specification 4.2"</i>. The following is an excerpt
 * from the document.
 * </p>
 *
 * <blockquote>
 * <p><i>
 * Each AD structure shall have a Length field of one octet, which contains
 * the Length value, and a Data field of Length octets. The first octet of
 * the Data field contains the AD type field. The content of the remaining
 * Length - 1 octet in the Data field depends on the value of the AD type
 * field and is called the AD data.
 * </i></p>
 * </blockquote>
 *
 * @see <a href="https://www.bluetooth.org/en-us/specification/adopted-specifications"
 *      >Specification Adopted Documents</a>
 */
public class ADStructure implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final String STRING_FORMAT = "ADStructure(Length=%d,Type=0x%02X)";

    private int mLength;
    private int mType;
    private byte[] mData;


    /**
     * Constructor to create an instance with length=0, type=0, and data=null.
     */
    public ADStructure()
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
     */
    public ADStructure(int length, int type, byte[] data)
    {
        mLength = length;
        mType   = type;
        mData   = data;
    }


    /**
     * Get the length of the AD structure.
     *
     * <p>
     * This method returns the value of the first octet of the AD structure.
     * </p>
     *
     * @return
     *         The length of the AD structure.
     */
    public int getLength()
    {
        return mLength;
    }


    /**
     * Set the length of the AD structure.
     *
     * @param length
     *         The length of the AD structure.
     */
    public void setLength(int length)
    {
        mLength = length;
    }


    /**
     * Get the AD type.
     *
     * <p>
     * The list of AD types are found at
     * <a href="https://www.bluetooth.org/en-us/specification/assigned-numbers/generic-access-profile"
     * >Generic Access Profile (GAP)</a>.
     * </p>
     *
     * @return
     *         The AD type.
     *
     * @see <a href="https://www.bluetooth.org/en-us/specification/assigned-numbers/generic-access-profile"
     *      >Generic Access Profile (GAP)</a>
     */
    public int getType()
    {
        return mType;
    }


    /**
     * Set the AD type.
     *
     * @param type
     *        The AD type.
     */
    public void setType(int type)
    {
        mType = type;
    }


    /**
     * Get the AD data.
     *
     * <p>
     * The content of the AD data depends on the AD type.
     * </p>
     *
     * @return
     *         The AD data.
     */
    public byte[] getData()
    {
        return mData;
    }


    /**
     * Set the AD data.
     *
     * @param data
     *         The AD data.
     */
    public void setData(byte[] data)
    {
        mData = data;
    }


    @Override
    public String toString()
    {
        return String.format(STRING_FORMAT, mLength, mType);
    }
}

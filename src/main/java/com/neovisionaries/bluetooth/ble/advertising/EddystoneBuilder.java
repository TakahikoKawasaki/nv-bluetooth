/*
 * Copyright (C) 2015-2016 Neo Visionaries Inc.
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
 * Builder for {@link Eddystone} instances.
 *
 * @since 1.5
 *
 * @see <a href="https://github.com/google/eddystone/blob/master/protocol-specification.md"
 *      >Eddystone Protocol Specification</a>
 */
class EddystoneBuilder implements ADStructureBuilder
{
    @Override
    public ADStructure build(int length, int type, byte[] data)
    {
        if (data == null || data.length < 3)
        {
            return null;
        }

        // If the 16-bit service UUID is not Eddystone UUID.
        if ((data[0] & 0xFF) != 0xAA || (data[1] & 0xFF) != 0xFE)
        {
            return null;
        }

        // From Eddystone Protocol Specification
        //
        //   The specific type of Eddystone frame is encoded in the high-order
        //   four bits of the first octet in the Service Data associated with
        //   the Service UUID. Permissible values are:
        //
        //       Frame Type | High-Order 4 bits | Byte Value
        //      ------------|-------------------|------------
        //       UID        |        0000       |     0x00
        //       URL        |        0001       |     0x10
        //       TLM        |        0010       |     0x20
        //       EID        |        0011       |     0x30
        //       RESERVED   |        0100       |     0x40
        //
        //   The four low-order bits are reserved for future use and must be 0000.
        //

        // Extract the frame type.
        int frameType = (data[2] & 0xF0);

        switch (frameType)
        {
            // Eddystone UID
            case 0x00:
                return new EddystoneUID(length, type, data);

            // Eddystone URL
            case 0x10:
                return new EddystoneURL(length, type, data);

            // Eddystone TLM
            case 0x20:
                return new EddystoneTLM(length, type, data);

            // Eddystone EID
            case 0x30:
                return new EddystoneEID(length, type, data);

            default:
                return null;
        }
    }
}

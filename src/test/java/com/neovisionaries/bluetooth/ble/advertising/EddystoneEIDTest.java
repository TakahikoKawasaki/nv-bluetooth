/*
 * Copyright (C) 2016 Neo Visionaries Inc.
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


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import com.neovisionaries.bluetooth.ble.advertising.Eddystone.FrameType;
import com.neovisionaries.bluetooth.ble.util.UUIDCreator;


public class EddystoneEIDTest
{
    @Test
    public void test1()
    {
        byte[] data = {
                (byte)13,                // Length
                (byte)0x16,              // AD Type = Service Data - 16-bit UUID
                (byte)0xAA, (byte)0xFE,  // Eddystone UUID
                (byte)0x30,              // Eddystone Frame Type = EID
                (byte)-90,               // Calibrated Tx power at 0 m
                (byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78,  // 8-byte EID
                (byte)0x9A, (byte)0xBC, (byte)0xDE, (byte)0xF0
        };

        List<ADStructure> list = ADPayloadParser.getInstance().parse(data);

        assertNotNull("ADPayloadParser.parse(byte[]) returned null.", list);
        assertEquals("Unexpected size of ADStructure list.", 1, list.size());

        // EddystoneUID
        assertTrue("Not an instance of EddystoneEID.", (list.get(0) instanceof EddystoneEID));
        EddystoneEID es = (EddystoneEID)list.get(0);

        // AD Type
        assertEquals("AD Type is not 0x16.", 0x16, es.getType());

        // Service UUID
        UUID uuid = UUIDCreator.from16(new byte[] { (byte)0xAA, (byte)0xFE });
        assertEquals("Service UUID is not for Eddystone.", uuid, es.getServiceUUID());

        // Frame Type
        assertSame("Unexpected Frame Type.", FrameType.EID, es.getFrameType());

        // Tx Power
        assertEquals("Unexpected Tx Power.", -90, es.getTxPower());

        // EID
        byte[] eid = {
                (byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78,
                (byte)0x9A, (byte)0xBC, (byte)0xDE, (byte)0xF0
        };
        assertArrayEquals("Unexpected EID.", eid, es.getEID());
        assertEquals("Unexpected EID (string).", "123456789ABCDEF0", es.getEIDAsString());
    }
}

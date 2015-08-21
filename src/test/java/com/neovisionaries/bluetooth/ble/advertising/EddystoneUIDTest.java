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


public class EddystoneUIDTest
{
    @Test
    public void test1()
    {
        byte[] data = {
                (byte)23,                // Length
                (byte)0x16,              // AD Type = Service Data - 16-bit UUID
                (byte)0xAA, (byte)0xFE,  // Eddystone UUID
                (byte)0x00,              // Eddystone Frame Type = UID
                (byte)-90,               // Calibrated Tx power at 0 m
                (byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78,  // 10-byte Namespace ID
                (byte)0x9A, (byte)0xBC, (byte)0xDE, (byte)0xF0,
                (byte)0x12, (byte)0x34,
                (byte)0xFE, (byte)0xDC, (byte)0xBA, (byte)0x98,  // 6-byte Instance ID
                (byte)0x76, (byte)0x54,
                (byte)0x00, (byte)0x00                           // Reserved
        };

        List<ADStructure> list = ADPayloadParser.getInstance().parse(data);

        assertNotNull("ADPayloadParser.parse(byte[]) returned null.", list);
        assertEquals("Unexpected size of ADStructure list.", 1, list.size());

        // EddystoneUID
        assertTrue("Not an instance of EddystoneUID.", (list.get(0) instanceof EddystoneUID));
        EddystoneUID es = (EddystoneUID)list.get(0);

        // AD Type
        assertEquals("AD Type is not 0x16.", 0x16, es.getType());

        // Service UUID
        UUID uuid = UUIDCreator.from16(new byte[] { (byte)0xAA, (byte)0xFE });
        assertEquals("Service UUID is not for Eddystone.", uuid, es.getServiceUUID());

        // Frame Type
        assertSame("Unexpected Frame Type.", FrameType.UID, es.getFrameType());

        // Tx Power
        assertEquals("Unexpected Tx Power.", -90, es.getTxPower());

        // Namespace ID
        byte[] namespaceId = {
                (byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78,
                (byte)0x9A, (byte)0xBC, (byte)0xDE, (byte)0xF0,
                (byte)0x12, (byte)0x34
        };
        assertArrayEquals("Unexpected Namespace ID.", namespaceId, es.getNamespaceId());
        assertEquals("Unexpected Namespace ID (string).", "123456789ABCDEF01234", es.getNamespaceIdAsString());

        // Instance ID
        byte[] instanceId = {
                (byte)0xFE, (byte)0xDC, (byte)0xBA, (byte)0x98,
                (byte)0x76, (byte)0x54
        };
        assertArrayEquals("Unexpected Instance ID.", instanceId, es.getInstanceId());
        assertEquals("Unexpected Instance ID (string).", "FEDCBA987654", es.getInstanceIdAsString());

        // Beacon ID
        byte[] beaconId = {
                (byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78,  // 10-byte Namespace ID
                (byte)0x9A, (byte)0xBC, (byte)0xDE, (byte)0xF0,
                (byte)0x12, (byte)0x34,
                (byte)0xFE, (byte)0xDC, (byte)0xBA, (byte)0x98,  // 6-byte Instance ID
                (byte)0x76, (byte)0x54
        };
        assertArrayEquals("Unexpected Beacon ID.", beaconId, es.getBeaconId());
        assertEquals("Unexpected Beacon ID (string).", "123456789ABCDEF01234FEDCBA987654", es.getBeaconIdAsString());
    }
}

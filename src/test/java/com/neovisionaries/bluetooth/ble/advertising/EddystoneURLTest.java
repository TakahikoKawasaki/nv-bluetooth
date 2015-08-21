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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import com.neovisionaries.bluetooth.ble.advertising.Eddystone.FrameType;
import com.neovisionaries.bluetooth.ble.util.UUIDCreator;


public class EddystoneURLTest
{
    private static EddystoneURL getEddystoneURL(byte[] data)
    {
        List<ADStructure> list = ADPayloadParser.getInstance().parse(data);

        return (EddystoneURL)list.get(0);
    }


    @Test
    public void test1()
    {
        byte[] data = {
                (byte)14,                // Length
                (byte)0x16,              // AD Type = Service Data - 16-bit UUID
                (byte)0xAA, (byte)0xFE,  // Eddystone UUID
                (byte)0x10,              // Eddystone Frame Type = URL
                (byte)-50,               // Calibrated Tx power at 0 m
                (byte)0x00,              // URL Scheme Prefix ("http://www.")
                (byte)'e',               // e
                (byte)'x',               // x
                (byte)'a',               // a
                (byte)'m',               // m
                (byte)'p',               // p
                (byte)'l',               // l
                (byte)'e',               // e
                (byte)0x00               // ".com/"
        };

        List<ADStructure> list = ADPayloadParser.getInstance().parse(data);

        assertNotNull("ADPayloadParser.parse(byte[]) returned null.", list);
        assertEquals("Unexpected size of ADStructure list.", 1, list.size());

        // EddystoneURL
        assertTrue("Not an instance of EddystoneURL.", (list.get(0) instanceof EddystoneURL));
        EddystoneURL es = (EddystoneURL)list.get(0);

        // AD Type
        assertEquals("AD Type is not 0x16.", 0x16, es.getType());

        // Service UUID
        UUID uuid = UUIDCreator.from16(new byte[] { (byte)0xAA, (byte)0xFE });
        assertEquals("Service UUID is not for Eddystone.", uuid, es.getServiceUUID());

        // Frame Type
        assertSame("Unexpected Frame Type.", FrameType.URL, es.getFrameType());

        // Tx Power
        assertEquals("Unexpected Tx Power.", -50, es.getTxPower());

        // URL
        assertEquals("Unexpected URL.", "http://www.example.com/", es.getURL().toExternalForm());
    }


    @Test
    public void test2()
    {
        byte[] data = {
                (byte)18,                // Length
                (byte)0x16,              // AD Type = Service Data - 16-bit UUID
                (byte)0xAA, (byte)0xFE,  // Eddystone UUID
                (byte)0x10,              // Eddystone Frame Type = URL
                (byte)-50,               // Calibrated Tx power at 0 m
                (byte)0x01,              // URL Scheme Prefix ("https://www.")
                (byte)'e',               // e
                (byte)'x',               // x
                (byte)'a',               // a
                (byte)'m',               // m
                (byte)'p',               // p
                (byte)'l',               // l
                (byte)'e',               // e
                (byte)0x01,              // ".org/"
                (byte)'t',               // t
                (byte)'e',               // e
                (byte)'s',               // s
                (byte)'t',               // t
        };

        EddystoneURL es = getEddystoneURL(data);

        // URL
        assertEquals("Unexpected URL.", "https://www.example.org/test", es.getURL().toExternalForm());
    }
}

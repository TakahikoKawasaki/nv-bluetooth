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


public class EddystoneTLMTest
{
    @Test
    public void test1()
    {
        byte[] data = {
                (byte)17,                                        // Length
                (byte)0x16,                                      // AD Type = Service Data - 16-bit UUID
                (byte)0xAA, (byte)0xFE,                          // Eddystone UUID
                (byte)0x20,                                      // Eddystone Frame Type = TLM
                (byte)0x00,                                      // TLM version (0)
                (byte)0x12, (byte)0x34,                          // Battery voltage (0x1234)
                (byte)0x14, (byte)0x80,                          // Beacon Temperature (20.5)
                (byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78,  // Advertisement count
                (byte)0x98, (byte)0x76, (byte)0x54, (byte)0x32   // Elapsed time
        };

        List<ADStructure> list = ADPayloadParser.getInstance().parse(data);

        assertNotNull("ADPayloadParser.parse(byte[]) returned null.", list);
        assertEquals("Unexpected size of ADStructure list.", 1, list.size());

        // EddystoneTLM
        assertTrue("Not an instance of EddystoneTLM.", (list.get(0) instanceof EddystoneTLM));
        EddystoneTLM es = (EddystoneTLM)list.get(0);

        // AD Type
        assertEquals("AD Type is not 0x16.", 0x16, es.getType());

        // Service UUID
        UUID uuid = UUIDCreator.from16(new byte[] { (byte)0xAA, (byte)0xFE });
        assertEquals("Service UUID is not for Eddystone.", uuid, es.getServiceUUID());

        // Frame Type
        assertSame("Unexpected Frame Type.", FrameType.TLM, es.getFrameType());

        // TLM version
        assertEquals("Unexpected TLM version.", 0x00, es.getTLMVersion());

        // Battery voltage.
        assertEquals("Unexpected Battery Voltage.", 0x1234, es.getBatteryVoltage());

        // Beacon Temperature
        assertEquals("Unexpected Beacon Temperature.", 20.5, es.getBeaconTemperature(), 0.01);

        // Advertisement count
        assertEquals("Unexpected Advertisement Count.", 0x12345678L, es.getAdvertisementCount());

        // Elapsed time
        assertEquals("Unexpected Elapsed Time.", 0x98765432L * 100, es.getElapsedTime());
    }
}

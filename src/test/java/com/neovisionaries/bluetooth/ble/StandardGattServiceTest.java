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
package com.neovisionaries.bluetooth.ble;


import static com.neovisionaries.bluetooth.ble.StandardGattService.ALERT_NOTIFICATION_SERVICE;
import static com.neovisionaries.bluetooth.ble.StandardGattService.AUTOMATION_IO;
import static com.neovisionaries.bluetooth.ble.StandardGattService.BATTERY_SERVICE;
import static com.neovisionaries.bluetooth.ble.StandardGattService.BLOOD_PRESSURE;
import static com.neovisionaries.bluetooth.ble.StandardGattService.BODY_COMPOSITION;
import static com.neovisionaries.bluetooth.ble.StandardGattService.BOND_MANAGEMENT;
import static com.neovisionaries.bluetooth.ble.StandardGattService.CONTINUOUS_GLUCOSE_MONITORING;
import static com.neovisionaries.bluetooth.ble.StandardGattService.getByUuid;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import java.util.UUID;
import org.junit.Test;


public class StandardGattServiceTest
{
    private void compareInstances(StandardGattService expected, StandardGattService actual)
    {
        if (expected == null)
        {
            assertNull(actual);
        }
        else
        {
            assertSame(expected, actual);
        }
    }


    private void uuidTest(String uuid, StandardGattService expected)
    {
        compareInstances(expected, getByUuid(uuid));
    }


    private void uuidTest(UUID uuid, StandardGattService expected)
    {
        compareInstances(expected, getByUuid(uuid));
    }


    @Test
    public void test01()
    {
        uuidTest("00001811-0000-1000-8000-00805f9b34fb", ALERT_NOTIFICATION_SERVICE);
    }


    @Test
    public void test02()
    {
        uuidTest("0000181500001000800000805f9b34fb", AUTOMATION_IO);
    }


    @Test
    public void test03()
    {
        uuidTest("0000180F-0000-1000-8000-00805F9B34FB", BATTERY_SERVICE);
    }


    @Test
    public void test04()
    {
        uuidTest("0000xxxx-0000-1000-8000-00805f9b34fb", null);
    }


    @Test
    public void test05()
    {
        uuidTest("00000000-0000-1000-8000-00805f9b34fb", null);
    }


    @Test
    public void test06()
    {
        uuidTest("10001811-0000-1000-8000-00805f9b34fb", null);
    }


    @Test
    public void test07()
    {
        assertEquals(0x1810, BLOOD_PRESSURE.getNumber());
    }


    @Test
    public void test08()
    {
        assertEquals("body_composition", BODY_COMPOSITION.getShortType());
    }


    @Test
    public void test09()
    {
        assertEquals("org.bluetooth.service.bond_management", BOND_MANAGEMENT.getType());
    }


    @Test
    public void test10()
    {
        uuidTest(UUID.fromString("0000181F-0000-1000-8000-00805f9b34fb"), CONTINUOUS_GLUCOSE_MONITORING);
    }
}

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


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Standard GATT services.
 *
 * @since 1.7
 *
 * @see <a href="https://developer.bluetooth.org/gatt/services/Pages/ServicesHome.aspx"
 *      >Bluetooth Developer Portal, Services</a>
 */
public enum StandardGattService
{
    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.alert_notification.xml"
     * >Alert Notification Service</a> (0x1811).
     */
    ALERT_NOTIFICATION_SERVICE("Alert Notification Service", "alert_notification", 0x1811),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.automation_io.xml"
     * >Automation IO</a> (0x1815).
     */
    AUTOMATION_IO("Automation IO", "automation_io", 0x1815),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.battery_service.xml"
     * >Battery Service</a> (0x180F).
     */
    BATTERY_SERVICE("Battery Service", "battery_service", 0x180F),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.blood_pressure.xml"
     * >Blood Pressure</a> (0x1810).
     */
    BLOOD_PRESSURE("Blood Pressure", "blood_pressure", 0x1810),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.body_composition.xml"
     * >Blood Composition</a> (0x181B).
     */
    BODY_COMPOSITION("Body Composition", "body_composition", 0x181B),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.bond_management.xml"
     * >Bond Management</a> (0x181E).
     */
    BOND_MANAGEMENT("Bond Management", "bond_management", 0x181E),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.continuous_glucose_monitoring.xml"
     * >Continuous Clucose Monitoring</a> (0x181F).
     */
    CONTINUOUS_GLUCOSE_MONITORING("Continuous Glucose Monitoring", "continuous_glucose_monitoring", 0x181F),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.current_time.xml"
     * >Current Time Service</a> (0x1805).
     */
    CURRENT_TIME_SERVICE("Current Time Service", "current_time", 0x1805),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.cycling_power.xml"
     * >Cycling Power</a> (0x1818).
     */
    CYCLING_POWER("Cycling Power", "cycling_power", 0x1818),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.cycling_speed_and_cadence.xml"
     * >Cycling Speed and Cadence</a> (0x1816).
     */
    CYCLING_SPEED_AND_CADENCE("Cycling Speed and Cadence", "cycling_speed_and_cadence", 0x1816),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.device_information.xml"
     * >Device Information</a> (0x180A).
     */
    DEVICE_INFORMATION("Device Information", "device_information", 0x180A),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.environmental_sensing.xml"
     * >Environmental Sensing</a> (0x181A).
     */
    ENVIRONMENTAL_SENSING("Environmental Sensing", "environmental_sensing", 0x181A),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.generic_access.xml"
     * >Generic Access</a> (0x1800).
     */
    GENERIC_ACCESS("Generic Access", "generic_access", 0x1800),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.generic_attribute.xml"
     * >Generic Attribute</a> (0x1801).
     */
    GENERIC_ATTRIBUTE("Generic Attribute", "generic_attribute", 0x1801),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.glucose.xml"
     * >Glucose</a> (0x1808).
     */
    GLUCOSE("Glucose", "glucose", 0x1808),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.health_thermometer.xml"
     * >Health Thermometer</a> (0x1809).
     */
    HEALTH_THERMOMETER("Health Thermometer", "health_thermometer", 0x1809),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.heart_rate.xml"
     * >Heart Rate</a> (0x180D).
     */
    HEART_RATE("Heart Rate", "heart_rate", 0x180D),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.http_proxy.xml"
     * >HTTP Proxy</a> (0x1823).
     */
    HTTP_PROXY("HTTP Proxy", "http_proxy", 0x1823),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.human_interface_device.xml"
     * >Human Interface Device</a> (0x1812).
     */
    HUMAN_INTERFACE_DEVICE("Human Interface Device", "human_interface_device", 0x1812),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.immediate_alert.xml"
     * >Immediate Alert</a> (0x1802).
     */
    IMMEDIATE_ALERT("Immediate Alert", "immediate_alert", 0x1802),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.indoor_positioning.xml"
     * >Indoor Positioning</a> (0x1821).
     */
    INDOOR_POSITIONING("Indoor Positioning", "indoor_positioning", 0x1821),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.internet_protocol_support.xml"
     * >Internet Protocol Support</a> (0x1820).
     */
    INTERNET_PROTOCOL_SUPPORT("Internet Protocol Support", "internet_protocol_support", 0x1820),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.link_loss.xml"
     * >Link Loss</a> (0x1803).
     */
    LINK_LOSS("Link Loss", "link_loss", 0x1803),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.location_and_navigation.xml"
     * >Location and Navigation</a> (0x1819).
     */
    LOCATION_AND_NAVIGATION("Location and Navigation", "location_and_navigation", 0x1819),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.next_dst_change.xml"
     * >Next DST Change Service</a> (0x1807).
     */
    NEXT_DST_CHANGE_SERVICE("Next DST Change Service", "next_dst_change", 0x1807),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.object_transfer.xml"
     * >Object Transfer</a> (0x1825).
     */
    OBJECT_TRANSFER("Object Transfer", "object_transfer", 0x1825),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.phone_alert_status.xml"
     * >Phone Alert Status Service</a> (0x180E).
     */
    PHONE_ALERT_STATUS_SERVICE("Phone Alert Status Service", "phone_alert_status", 0x180E),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.pulse_oximeter.xml"
     * >Pulse Oximeter</a> (0x1822).
     */
    PULSE_OXIMETER("Pulse Oximeter", "pulse_oximeter", 0x1822),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.reference_time_update.xml"
     * >Reference Time Update Service</a> (0x1806).
     */
    REFERENCE_TIME_UPDATE_SERVICE("Reference Time Update Service", "reference_time_update", 0x1806),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.running_speed_and_cadence.xml"
     * >Running Speed and Cadence</a> (0x1814).
     */
    RUNNING_SPEED_AND_CADENCE("Running Speed and Cadence", "running_speed_and_cadence", 0x1814),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.scan_parameters.xml"
     * >Scan Parameters</a> (0x1813).
     */
    SCAN_PARAMETERS("Scan Parameters", "scan_parameters", 0x1813),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.transport_discovery.xml"
     * >Transport Discovery</a> (0x1824).
     */
    TRANSPORT_DISCOVERY("Transport Discovery", "transport_discovery", 0x1824),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.tx_power.xml"
     * >Tx Power</a> (0x1804).
     */
    TX_POWER("Tx Power", "tx_power", 0x1804),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.user_data.xml"
     * >User Data</a> (0x181C).
     */
    USER_DATA("User Data", "user_data", 0x181C),


    /**
     * <a href="https://developer.bluetooth.org/gatt/services/Pages/ServiceViewer.aspx?u=org.bluetooth.service.weight_scale.xml"
     * >Weight Scale</a> (0x181D).
     */
    WEIGHT_SCALE("Weight Scale", "weight_scale", 0x181D);
    ;


    private static final String SPECIFICATION_TYPE_PREFIX = "org.bluetooth.service.";
    private static final Pattern UUID_PATTERN =
            Pattern.compile("0000([0-9a-fA-F]{4})[-]?0000[-]?1000[-]?8000[-]?00805[fF]9[bB]34[fF][bB]");
    private static final Map<Integer, StandardGattService> sNumberToInstanceMap;


    static
    {
        sNumberToInstanceMap = new HashMap<Integer, StandardGattService>();

        for (StandardGattService instance : values())
        {
            sNumberToInstanceMap.put(Integer.valueOf(instance.getNumber()), instance);
        }
    }


    private final String mName;
    private final String mShortType;
    private final int mNumber;
    private transient String mType;


    /**
     * The private constructor.
     */
    private StandardGattService(String name, String shortType, int number)
    {
        mName      = name;
        mShortType = shortType;
        mNumber    = number;
    }


    /**
     * Get the specification name.
     *
     * @return
     *         The specification name. For example,
     *         {@code "Alert Notification Service"}.
     */
    public String getName()
    {
        return mName;
    }


    /**
     * Get the specification type without the common prefix
     * {@code "org.bluetooth.service."}.
     *
     * @return
     *         The specification type without the common prefix.
     *         For example, {@code "alert_notification"}.
     */
    public String getShortType()
    {
        return mShortType;
    }


    /**
     * Get the specification type.
     *
     * @return
     *         The specification type. For example,
     *         {@code "org.bluetooth.service.alert_notification"}.
     */
    public String getType()
    {
        if (mType != null)
        {
            return mType;
        }

        synchronized (this)
        {
            if (mType == null)
            {
                mType = SPECIFICATION_TYPE_PREFIX + mShortType;
            }
        }

        return mType;
    }


    /**
     * Get the assigned number.
     *
     * @return
     *         The assigned number. For example, 0x1811.
     */
    public int getNumber()
    {
        return mNumber;
    }


    /**
     * Get a {@link StandardGattService} instance by an assigned number.
     *
     * @param number
     *         An assigned number. For example, 0x1811.
     *
     * @return
     *         A {@link StandardGattService} instance that has the assigned number.
     *         {@code null} is returned if no instance has the assigned number.
     */
    public static StandardGattService getByNumber(int number)
    {
        return sNumberToInstanceMap.get(Integer.valueOf(number));
    }


    /**
     * Get a {@link StandardGattService} instance by a UUID.
     *
     * @param uuid
     *         UUID. For example, {@code "00001811-0000-1000-8000-00805f9b34fb"}.
     *         Case-insensitive. Hyphens can be omitted.
     *
     * @return
     *         A {@link StandardGattService} instance that has the UUID.
     *         {@code null} is returned if no instance has the UUID.
     */
    public static StandardGattService getByUuid(String uuid)
    {
        if (uuid == null)
        {
            return null;
        }

        Matcher matcher = UUID_PATTERN.matcher(uuid);

        if (matcher.matches() == false)
        {
            return null;
        }

        int number;

        try
        {
            number = Integer.parseInt(matcher.group(1), 16);
        }
        catch (Exception e)
        {
            return null;
        }

        return getByNumber(number);
    }


    /**
     * Get a {@link StandardGattService} instance by a UUID.
     *
     * @param uuid
     *         UUID.
     *
     * @return
     *         A {@link StandardGattService} instance that has the UUID.
     *         {@code null} is returned if no instance has the UUID.
     */
    public static StandardGattService getByUuid(UUID uuid)
    {
        if (uuid == null)
        {
            return null;
        }

        return getByUuid(uuid.toString());
    }
}

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
package com.neovisionaries.bluetooth.ble;


import java.util.HashMap;
import java.util.Map;


/**
 * This enum represents success code and error codes defined in
 * <code>external/<wbr>bluetooth/<wbr>bluedroid/<wbr>stack/<wbr>include/<wbr></code>{@code gatt_api.h}.
 *
 * <p>
 * The {@code status} parameter in <a
 * href="http://developer.android.com/reference/android/bluetooth/BluetoothGattCallback.html"
 * >BluetoothGattCallback</a> class is one of the entries in this enum (probably).
 * </p>
 *
 * <p>
 * NOTE: This class was moved from {@code com.neovisionaries.bluetooth.ble.util}
 * package to {@code com.neovisionaries.bluetooth.ble} package on the release of
 * version 1.7.
 * </p>
 *
 * @since 1.1
 */
public enum GattStatusCode
{
    /**
     * GATT_SUCCESS [0x00, 0].
     *
     * <p>
     * GATT_ENCRYPTED_MITM has the same value as GATT_SUCCESS.
     * </p>
     */
    SUCCESS(0x00),


    /**
     * GATT_INVALID_HANDLE [0x01, 1].
     */
    INVALID_HANDLE(0x01),


    /**
     * GATT_READ_NOT_PERMIT [0x02, 2].
     */
    READ_NOT_PERMIT(0x02),


    /**
     * GATT_WRITE_NOT_PERMIT [0x03, 3].
     */
    WRITE_NOT_PERMIT(0x03),


    /**
     * GATT_INVALID_PDU [0x04, 4].
     */
    INVALID_PDU(0x04),


    /**
     * GATT_INSUF_AUTHENTICATION [0x05, 5].
     */
    INSUF_AUTHENTICATION(0x05),


    /**
     * GATT_REQ_NOT_SUPPORTED [0x06, 6].
     */
    REQ_NOT_SUPPORTED(0x06),


    /**
     * GATT_INVALID_OFFSET [0x07, 7].
     */
    INVALID_OFFSET(0x07),


    /**
     * GATT_INSUF_AUTHORIZATION [0x08, 8].
     */
    INSUF_AUTHORIZATION(0x08),


    /**
     * GATT_PREPARE_Q_FULL [0x09, 9].
     */
    PREPARE_Q_FULL(0x09),


    /**
     * GATT_NOT_FOUND [0x0a, 10].
     */
    NOT_FOUND(0x0a),


    /**
     * GATT_NOT_LONG [0x0b, 11].
     */
    NOT_LONG(0x0b),


    /**
     * GATT_INSUF_KEY_SIZE [0x0c, 12].
     */
    INSUF_KEY_SIZE(0x0c),


    /**
     * GATT_INVALID_ATTR_LEN [0x0d, 13].
     */
    INVALID_ATTR_LEN(0x0d),


    /**
     * GATT_ERR_UNLIKELY [0x0e, 14].
     */
    ERR_UNLIKELY(0x0e),


    /**
     * GATT_INSUF_ENCRYPTION [0x0f, 15].
     */
    INSUF_ENCRYPTION(0x0f),


    /**
     * GATT_UNSUPPORT_GRP_TYPE [0x10, 16].
     */
    UNSUPPORT_GRP_TYPE(0x10),


    /**
     * GATT_INSUF_RESOURCE [0x11, 17].
     */
    INSUF_RESOURCE(0x11),


    /**
     * GATT_NO_RESOURCES [0x80, 128].
     */
    NO_RESOURCES(0x80),


    /**
     * GATT_INTERNAL_ERROR [0x81, 129].
     */
    INTERNAL_ERROR(0x81),


    /**
     * GATT_WRONG_STATE [0x82, 130].
     */
    WRONG_STATE(0x82),


    /**
     * GATT_DB_FULL [0x83, 131].
     */
    DB_FULL(0x83),


    /**
     * GATT_BUSY [0x84, 132].
     */
    BUSY(0x84),


    /**
     * GATT_ERROR [0x85, 133].
     */
    ERROR(0x85),


    /**
     * GATT_CMD_STARTED [0x86, 134].
     */
    CMD_STARTED(0x86),


    /**
     * GATT_ILLEGAL_PARAMETER [0x87, 135].
     */
    ILLEGAL_PARAMETER(0x87),


    /**
     * GATT_PENDING [0x88, 136].
     */
    PENDING(0x88),


    /**
     * GATT_AUTH_FAIL [0x89, 137].
     */
    AUTH_FAIL(0x89),


    /**
     * GATT_MORE [0x8a, 138].
     */
    MORE(0x8a),


    /**
     * GATT_INVALID_CFG [0x8b, 139].
     */
    INVALID_CFG(0x8b),


    /**
     * GATT_SERVICE_STARTED [0x8c, 140].
     */
    SERVICE_STARTED(0x8c),


    /**
     * GATT_ENCRYPTED_NO_MITM [0x8d, 141].
     */
    ENCRYPTED_NO_MITM(0x8d),


    /**
     * GATT_NOT_ENCRYPTED [0x8e, 142].
     */
    NOT_ENCRYPTED(0x8e),


    /**
     * GATT_CONGESTED [0x8f, 143].
     */
    CONGESTED(0x8f),


    /**
     * GATT_CCC_CFG_ERR [0xfd, 253].
     */
    CCC_CFG_ERR(0xfd),


    /**
     * GATT_PRC_IN_PROGRESS [0xfe, 254].
     */
    PRC_IN_PROGRESS(0xfe),


    /**
     * GATT_OUT_OF_RANGE [0xff, 255].
     */
    OUT_OF_RANGE(0xff)
    ;


    private static final Map<Integer, GattStatusCode> sValueToCodeMap;
    private final int mValue;


    static
    {
        sValueToCodeMap = new HashMap<Integer, GattStatusCode>();

        for (GattStatusCode code : values())
        {
            sValueToCodeMap.put(Integer.valueOf(code.getValue()), code);
        }
    }


    private GattStatusCode(int value)
    {
        mValue = value;
    }


    /**
     * Get the integer value of this result code.
     *
     * @return
     *         The integer value of this result code.
     */
    public int getValue()
    {
        return mValue;
    }


    /**
     * Get a {@link GattStatusCode} instance that has the specified value.
     *
     * @param value
     *         The integer value of a result code.
     *
     * @return
     *         A {@link GattStatusCode} instance or {@code null}.
     */
    public static GattStatusCode getByValue(int value)
    {
        return sValueToCodeMap.get(Integer.valueOf(value));
    }
}

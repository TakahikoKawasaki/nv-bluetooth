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


/**
 * The interface that a builder of AD structure has to implement.
 */
public interface ADStructureBuilder
{
    /**
     * Build an AD structure.
     *
     * @param length
     *         The length of the AD structure. It is the value
     *         of the first octet of the byte sequence of the
     *         AD structure.
     *
     * @param type
     *         The AD type. It is the value of the second octet
     *         of the byte sequence of the AD structure.
     *
     * @param data
     *         The AD data. It is the third and following octets
     *         of the byte sequence of the AD structure.
     *
     * @return
     *         An instance of {@link ADStructure} or its subclass.
     *         If the builder fails to build an instance,
     *         {@code null} should be returned.
     */
    ADStructure build(int length, int type, byte[] data);
}

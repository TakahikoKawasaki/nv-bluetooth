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


import java.util.ArrayList;
import java.util.List;


/**
 * The base class for {@link ADManufacturerSpecificBuilder} implementations.
 */
public class MSBuilder implements ADManufacturerSpecificBuilder
{
    private final List<ADManufacturerSpecificBuilder> mBuilders =
        new ArrayList<ADManufacturerSpecificBuilder>();


    public MSBuilder()
    {
    }


    public MSBuilder(ADManufacturerSpecificBuilder... builders)
    {
        for (ADManufacturerSpecificBuilder builder : builders)
        {
            mBuilders.add(builder);
        }
    }


    @Override
    public ADManufacturerSpecific build(int length, int type, byte[] data, int companyId)
    {
        // For each builder.
        for (ADManufacturerSpecificBuilder builder : mBuilders)
        {
            // Let the builder build an AD structure.
            ADManufacturerSpecific structure = builder.build(length, type, data, companyId);

            // If the builder succeeded in building an AD structure.
            if (structure != null)
            {
                return structure;
            }
        }

        // None of the builders succeeded in building an AD structure.
        return null;
    }


    public void addBuilder(ADManufacturerSpecificBuilder builder)
    {
        if (builder == null)
        {
            return;
        }

        mBuilders.add(builder);
    }


    public void removeBuilder(ADManufacturerSpecificBuilder builder)
    {
        if (builder == null)
        {
            return;
        }

        mBuilders.remove(builder);
    }
}

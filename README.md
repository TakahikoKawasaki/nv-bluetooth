nv-bluetooth
============

Overview
--------

Bluetooth utility library, mainly for Android.


License
-------

Apache License, Version 2.0


Maven
-----

```xml
<dependency>
    <groupId>com.neovisionaries</groupId>
    <artifactId>nv-bluetooth</artifactId>
    <version>1.2</version>
</dependency>
```


Source Download
---------------

    git clone https://github.com/TakahikoKawasaki/nv-bluetooth.git


JavaDoc
-------

[JavaDoc of nv-bluetooth](http://TakahikoKawasaki.github.io/nv-bluetooth/)


Supported AD Types
------------------

 Value | Name
-------|------------------------------------------------
 0x01  | Flags
 0x02  | Incomplete List of 16-bit Service Class UUIDs
 0x03  | Complete List of 16-bit Service Class UUIDs
 0x04  | Incomplete List of 32-bit Service Class UUIDs
 0x05  | Complete List of 32-bit Service Class UUIDs
 0x06  | Incomplete List of 128-bit Service Class UUIDs
 0x07  | Complete List of 128-bit Service Class UUIDs
 0x14  | List of 16-bit Service Solicitation UUIDs
 0x15  | List of 128-bit Service Solicitation UUIDs
 0x1F  | List of 32-bit Service Solicitation UUIDs
 0xFF  | Manufacturer Specific Data


Supported Manufacturer Specific Data
------------------------------------

 Company ID | Company Name                                | Format
------------|---------------------------------------------|---------
 0x004C     | Apple, Inc.                                 | iBeacon
 0x0105     | Ubiquitous Computing Technology Corporation | ucode
 0x019A     | T-Engine Forum                              | ucode


Example
-------
```java
public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
{
    // Parse the payload of the advertising packet.
    List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord);

    // For each AD structure contained in the advertising packet.
    for (ADStructure structure : structures)
    {
        if (structure instanceof IBeacon)
        {
            // An iBeacon packet was found.
            handleIBeacon((IBeacon)structure);
        }
        else if (structure instanceof Ucode)
        {
            // A ucode packet was found.
            handleUcode((Ucode)structure);
        }
        else if (structure instanceof ADFlags)
        {
            handleFlags((ADFlags)structure);
        }
    }
}

private void handleIBeacon(IBeacon iBeacon)
{
    // Proximity UUID
    UUID uuid = iBeacon.getUUID();

    // Major number
    int major = iBeacon.getMajor();

    // Minor number
    int minor = iBeacon.getMinor();

    // Power
    int power = iBeacon.getPower();

    ......
}

private void handleUcode(Ucode ucode)
{
    // Version
    int version = ucode.getVersion();

    // Ucode (32 upper-case hex letters)
    String ucode = ucode.getUcode();

    // Status
    int status = ucode.getStatus();

    // The state of the battery
    boolean low = ucode.isBatteryLow();

    // Transmission interval
    int interval = ucode.getInterval();

    // Transmission power
    int power = ucode.getPower();

    // Transmission count
    int count = ucode.getCount();

    ......
}


private void handleFlags(ADFlags flags)
{
    // LE Limited Discoverable Mode
    boolean limited = flags.isLimitedDiscoverable();

    // LE General Discoverable Mode
    boolean general = flags.isGeneralDiscoverable();

    // (inverted) BR/EDR Not Supported
    boolean legacySupported = flags.isLegacySupported();

    // Simultaneous LE and BR/EDR to Same Device Capable (Controller)
    boolean controllerSimultaneity = flags.isControllerSimultaneitySupported();

    // Simultaneous LE and BR/EDR to Same Device Capable (Host)
    boolean hostSimultaneity = flags.isHostSimultaneitySupported();

    ......
}
```

```java
public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
{
    Log.d(TAG, "status = " + stringifyGattStatus(status));

    ......
}

private static String stringifyGattStatus(int status)
{
    GattStatusCode code = GattStatusCode.getByValue(status);

    if (code != null)
    {
        return code.name();
    }
    else
    {
        return String.format("UNKNOWN (%d)", status);
    }
}
```


Note
----

Not tested enough.


TODO
----

* Support more standard AD structures.
* Testing.


Author
------

Takahiko Kawasaki, Neo Visionaries Inc.

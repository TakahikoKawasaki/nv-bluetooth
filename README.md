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
    <version>1.1</version>
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

 Company ID | Company Name   | Format
------------|----------------|-----------------------------
 0x004C     | Apple, Inc.    | iBeacon
 0x019A     | T-Engine Forum | ucode


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
            IBeacon iBeacon = (IBeacon)structure;

            // Proximity UUID, major number, minor number and power.
            UUID uuid = iBeacon.getUUID();
            int major = iBeacon.getMajor();
            int minor = iBeacon.getMinor();
            int power = iBeacon.getPower();
```


Note
----

Not tested.


TODO
----

* Support more standard AD structures.
* Testing.


Author
------

Takahiko Kawasaki, Neo Visionaries Inc.

nv-bluetooth
============

Overview
--------

Bluetooth utility library, mainly for Android.

The main purpose of this library is to provide a parser for the payload
part of BLE advertising packets. According to the specification, the
payload part should be parsed as a list of _AD Structures_. This library
does it correctly.


License
-------

Apache License, Version 2.0


Maven
-----

```xml
<dependency>
    <groupId>com.neovisionaries</groupId>
    <artifactId>nv-bluetooth</artifactId>
    <version>1.4</version>
</dependency>
```


Gradle
-----

```Gradle
dependencies {
    compile 'com.neovisionaries:nv-bluetooth:1.4'
}
```


Source Download
---------------

    git clone https://github.com/TakahikoKawasaki/nv-bluetooth.git


JavaDoc
-------

[JavaDoc of nv-bluetooth](http://TakahikoKawasaki.github.io/nv-bluetooth/)


Supported AD Types
------------------

 Value | Name                                           | Implementation Class      |
-------|------------------------------------------------|---------------------------|
 0x01  | Flags                                          | `Flags`                   |
 0x02  | Incomplete List of 16-bit Service Class UUIDs  | `UUIDs`                   |
 0x03  | Complete List of 16-bit Service Class UUIDs    | `UUIDs`                   |
 0x04  | Incomplete List of 32-bit Service Class UUIDs  | `UUIDs`                   |
 0x05  | Complete List of 32-bit Service Class UUIDs    | `UUIDs`                   |
 0x06  | Incomplete List of 128-bit Service Class UUIDs | `UUIDs`                   |
 0x07  | Complete List of 128-bit Service Class UUIDs   | `UUIDs`                   |
 0x08  | Shortened Local Name                           | `LocalName`               |
 0x09  | Complete Local Name                            | `LocalName`               |
 0x0A  | Tx Power Level                                 | `TxPowerLevel`            |
 0x14  | List of 16-bit Service Solicitation UUIDs      | `UUIDs`                   |
 0x15  | List of 128-bit Service Solicitation UUIDs     | `UUIDs`                   |
 0x1F  | List of 32-bit Service Solicitation UUIDs      | `UUIDs`                   |
 0xFF  | Manufacturer Specific Data                     | `ADManufacturereSpecific` |

The assigned numbers of AD types are listed in "[Generic Access Profile]
(https://www.bluetooth.org/en-us/specification/assigned-numbers/generic-access-profile)"
page.


Supported Manufacturer Specific Data
------------------------------------

 Company ID | Company Name                                | Format  | Implementation Class |
------------|---------------------------------------------|---------|----------------------|
 0x004C     | Apple, Inc.                                 | iBeacon | `IBeacon`            |
 0x0105     | Ubiquitous Computing Technology Corporation | ucode   | `Ucode`              |
 0x019A     | T-Engine Forum                              | ucode   | `Ucode`              |


Description
-----------

`ADPayloadParser` is a parser for the payload part of BLE advertising packets.
Its `parse` method parses a byte array as a list of AD Structures and returns
a list of `ADStructure` instances. The following is an example to parse the
payload part of an advertising packet.

```java
// onLeScan() method of BluetoothAdapter.LeScanCallback interface.
public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
{
    // Parse the payload of the advertising packet.
    List<ADStructure> structures =
        ADPayloadParser.getInstance().parse(scanRecord);
```

Each `ADStructure` instance may be able to be cast to a subclass. For example,
if an instance represents an iBeacon, it can be cast to `IBeacon` class. The
code example below checks if an `ADStructure` instance can be cast to
`IBeacon` by using `instanceof`.

```java
// For each AD structure contained in the advertising packet.
for (ADStructure structure : structures)
{
    // If the ADStructure instance can be cast to IBeacon.
    if (structure instanceof IBeacon)
    {
        // An iBeacon was found.
        IBeacon iBeacon = (IBeacon)structure;
        ......
    }
```

Subclasses of `ADStructure` class have their own specialized methods. For
instance, `IBeacon` class provides methods to get (1) the proximity UUID,
(2) the major number, (3) the minor number, and (4) the tx power.

```java
IBeacon iBeacon = (IBeacon)structure;

// (1) Proximity UUID
UUID uuid = iBeacon.getUUID();

// (2) Major number
int major = iBeacon.getMajor();

// (3) Minor number
int minor = iBeacon.getMinor();

// (4) Tx Power
int power = iBeacon.getPower();
```

The following shows the usage of `Flags` class's methods.

```java
Flags flags = (Flags)structure;

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
```

And the usage of `Ucode` class's methods. FYI: [ucode]
(http://en.wikipedia.org/wiki/Ucode_system) is an identification number
system that has officially been defined as "ITU-T H.642".

```java
Ucode ucode = (Ucode)structure;

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
```

This library contains some utility classes. `GattStatusCode` is an enum
that represents result codes of GATT API (which are defined in `gatt_api.h`).
Using the enum, a result code can be converted to a string like below.
Note that Android's `BluetoothGatt` class contains some result code
constants but many others are not defined.

```java
// onConnectionStateChange() method of BluetoothGattCallback class.
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


See Also
--------

* Assigned Numbers / [Generic Access Profile](https://www.bluetooth.org/en-us/specification/assigned-numbers/generic-access-profile)


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

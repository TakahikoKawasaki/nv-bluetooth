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
    <version>1.8</version>
</dependency>
```


Gradle
-----

```Gradle
dependencies {
    compile 'com.neovisionaries:nv-bluetooth:1.8'
}
```


Source Code
-----------

  <code>https://github.com/TakahikoKawasaki/nv-bluetooth</code>


JavaDoc
-------

  <code>http://TakahikoKawasaki.github.io/nv-bluetooth/</code>


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
 0x16  | Service Data - 16-bit UUID                     | `ServiceData`             |
 0x1F  | List of 32-bit Service Solicitation UUIDs      | `UUIDs`                   |
 0x20  | Service Data - 32-bit UUID                     | `ServiceData`             |
 0x21  | Service Data - 128-bit UUID                    | `ServiceData`             |
 0xFF  | Manufacturer Specific Data                     | `ADManufacturerSpecific`  |

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


Supported Service Data
----------------------

 Service UUID       | Format        | Implementation Class |
--------------------|---------------|----------------------|
 0xFEAA (Eddystone) | Eddystone UID | `EddystoneUID`       |
 0xFEAA (Eddystone) | Eddystone URL | `EddystoneURL`       |
 0xFEAA (Eddystone) | Eddystone TLM | `EddystoneTLM`       |
 0xFEAA (Eddystone) | Eddystone EID | `EddystoneEID`       |


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


#### iBeacon

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


#### Eddystone

There are four `ADStructure` subclasses for [Eddystone](https://github.com/google/eddystone).
`EddystoneUID` class is for [Eddystone UID](https://github.com/google/eddystone/tree/master/eddystone-uid),
`EddystoneURL` class is for [Eddystone URL](https://github.com/google/eddystone/tree/master/eddystone-url),
`EddystoneTLM` class is for [Eddystone TLM](https://github.com/google/eddystone/tree/master/eddystone-tlm), and
`EddystoneEID` class is for [Eddystone EID](https://github.com/google/eddystone/tree/master/eddystone-eid).
The exact inheritance tree is illustrated below.

```
ADStructure
  |
  +-- ServiceData
        |
        +-- Eddystone
              |
              +-- EddystoneUID
              +-- EddystoneURL
              +-- EddystoneTLM
              +-- EddystoneEID
```

```java
// Eddystone UID
EddystoneUID es = (EddystoneUID)structure;

// (1) Calibrated Tx power at 0 m.
int power = es.getTxPower();

// (2) 10-byte Namespace ID
byte[] namespaceId = es.getNamespaceId();
String namespaceIdAsString = es.getNamespaceIdAsString();

// (3) 6-byte Instance ID
byte[] instanceId = es.getInstanceId();
String instanceIdAsString = es.getInstanceIdAsString();

// (4) 16-byte Beacon ID
byte[] beaconId = es.getBeaconId();
String beaconIdAsString = es.getBeaconIdAsString();
```

```java
// Eddystone URL
EddystoneURL es = (EddystoneURL)structure;

// (1) Calibrated Tx power at 0 m.
int power = es.getTxPower();

// (2) URL
URL url = es.getURL();
```

```java
// Eddystone TLM
EddystoneTLM es = (EddystoneTLM)structure;

// (1) TLM Version
int version = es.getTLMVersion();

// (2) Battery Voltage
int voltage = es.getBatteryVoltage();

// (3) Beacon Temperature
float temperature = es.getBeaconTemperature();

// (4) Advertisement count since power-on or reboot.
long count = es.getAdvertisementCount();

// (5) Elapsed time in milliseconds since power-on or reboot.
long elapsed = es.getElapsedTime();
```

```java
// Eddystone EID
EddystoneEID es = (EddystoneEID)structure;

// (1) Calibrated Tx power at 0 m.
int power = es.getTxPower();

// (2) 8-byte EID
byte[] eid = es.getEID();
String eidAsString = es.getEIDAsString();
```


#### Flags

The following shows the usage of `Flags` class's methods.

```java
Flags flags = (Flags)structure;

// (1) LE Limited Discoverable Mode
boolean limited = flags.isLimitedDiscoverable();

// (2) LE General Discoverable Mode
boolean general = flags.isGeneralDiscoverable();

// (3) (inverted) BR/EDR Not Supported
boolean legacySupported = flags.isLegacySupported();

// (4) Simultaneous LE and BR/EDR to Same Device Capable (Controller)
boolean controllerSimultaneity = flags.isControllerSimultaneitySupported();

// (5) Simultaneous LE and BR/EDR to Same Device Capable (Host)
boolean hostSimultaneity = flags.isHostSimultaneitySupported();
```


#### ucode

Below is the usage of `Ucode` class's methods. FYI: [ucode]
(http://en.wikipedia.org/wiki/Ucode_system) is an identification number
system that has officially been defined as "ITU-T H.642".

```java
Ucode ucode = (Ucode)structure;

// (1) Version
int version = ucode.getVersion();

// (2) Ucode (32 upper-case hex letters)
String ucode = ucode.getUcode();

// (3) Status
int status = ucode.getStatus();

// (4) The state of the battery
boolean low = ucode.isBatteryLow();

// (5) Transmission interval
int interval = ucode.getInterval();

// (6) Transmission power
int power = ucode.getPower();

// (7) Transmission count
int count = ucode.getCount();
```


#### GATT status code

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


#### Support Your Own Format

You may want to handle AD structures whose formats are not standard ones.

This happens typically when your company makes beacons that emit special
packets. In this case, as the first step, your company asks Bluetooth SIG
to assign a new company ID for your company. Then, your company defines a
special format as a kind of Manufacturer Specific Data. The assigned
company ID is embedded in the format.

An AD structure whose format is defined by a company looks like below.

| Length | AD Type | Company ID |   Special Format  |
|:------:|:-------:|:----------:|:-----------------:|
| 1 byte | 1 byte  |   2 bytes  | (Length - 3) bytes|


The first byte holds the value of (the length of the AD structure - 1).
The second byte represents the AD type of the AD structure. When an AD
structure is defined by a company, its value is `0xFF`. This value means
_Manufacturer Specific Data_ and you can find it listed at the bottom
of the table in [Generic Access Profile]
(https://www.bluetooth.org/en-us/specification/assigned-numbers/generic-access-profile)
page.

When AD Type is `0xFF`, the following two bytes represent a company ID.
You can find the list of assigned company IDs in [Company Identifiers]
(https://www.bluetooth.org/en-us/specification/assigned-numbers/company-identifiers)
page. For example, `0x004C` represents Apple, Inc.

The format of the remaining bytes after the company ID field is defined
freely by the company. For example, iBeacon is a format defined by Apple.

In any case, you can register a parser for your special format into
`ADPayloadParser`. All you have to do is to implement a class that
implements `ADManufacturerSpecificBuilder` interface and to register it
by `ADPayloaderParser.registerManufacturerSpecificBuilder()`.

The following code is a real example in nv-bluetooth. `UcodeBuilder`
builds an instance of `Ucode` class from AD data (byte array). `Ucode`
class itself is a subclass of `ADManufacturerSpecific` class.

```java
class UcodeBuilder implements ADManufacturerSpecificBuilder
{
    @Override
    public ADManufacturerSpecific build(int length, int type, byte[] data, int companyId)
    {
        return Ucode.create(length, type, data, companyId);
    }
}
```

This builder can be registered as follows.

```java
// Register UcodeBuilder for the company ID 0x019A (T-Engine Forum).
// (The equivalent is done in the constructor of ADPayloadParser, though.)
ADPayloadParser.getInstance()
    .registerManufacturerSpecificBuilder(0x019A, new UcodeBuilder());
```

After this, you can write a code like below.

```java
List<ADStructure> structures
    = ADPayloadParser.getInstance().parse(payload);

for (ADStructure structure : structures)
{
    // If the AD structure can be cast to Ucode.
    if (structure instanceof Ucode)
    {
        Ucode ucode = (Ucode)structure;
    }
}
```

If you want to register a parser that is not for Manufacture Specific Data,
implement `ADStructureBuilder` interface instead of `ADManufacturerSpecificBuilder`.
See the implementation of `TxPowerLevelBuilder` and `TxPowerLevel` as they
are the simplest examples.


See Also
--------

* Bluetooth: [Specification Adopted Documents](https://www.bluetooth.org/en-us/specification/adopted-specifications)
* Bluetooth: Assigned Numbers / [Generic Access Profile](https://www.bluetooth.org/en-us/specification/assigned-numbers/generic-access-profile)
* Bluetooth: Assigned Numbers / [Company Identifiers](https://www.bluetooth.org/en-us/specification/assigned-numbers/company-identifiers)
* [Eddystone](https://github.com/google/eddystone)
* [ucode](http://en.wikipedia.org/wiki/Ucode_system)


TODO
----

* Support more standard AD structures.
* Testing.


Author
------

[Authlete, Inc.](https://www.authlete.com/) & Neo Visionaries Inc.<br/>
Takahiko Kawasaki &lt;taka@authlete.com&gt;

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
    <version>1.5</version>
</dependency>
```


Gradle
-----

```Gradle
dependencies {
    compile 'com.neovisionaries:nv-bluetooth:1.5'
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

There are three `ADStructure` subclasses for [Eddystone](https://github.com/google/eddystone).
`EddystoneUID` class is for [Eddystone UID](https://github.com/google/eddystone/tree/master/eddystone-uid),
`EddystoneURL` class is for [Eddystone URL](https://github.com/google/eddystone/tree/master/eddystone-url), and
`EddystoneTLM` class is for [Eddystone TLM](https://github.com/google/eddystone/tree/master/eddystone-tlm).
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


See Also
--------

* Bluetooth: [Specification Adopted Documents](https://www.bluetooth.org/en-us/specification/adopted-specifications)
* Bluetooth: Assigned Numbers / [Generic Access Profile](https://www.bluetooth.org/en-us/specification/assigned-numbers/generic-access-profile)
* [Eddystone](https://github.com/google/eddystone)
* [ucode](http://en.wikipedia.org/wiki/Ucode_system)


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

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
    <version>1.0</version>
</dependency>
```


Source Download
---------------

    git clone https://github.com/TakahikoKawasaki/nv-bluetooth.git


JavaDoc
-------

[JavaDoc of nv-bluetooth](http://TakahikoKawasaki.github.io/nv-bluetooth/)


Example
-------
```java
public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
{
    // Parse the payload of the advertising packet.
    List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord);
}
```


Author
------

Takahiko Kawasaki, Neo Visionaries Inc.

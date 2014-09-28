Java bencode serializator
=========================

This is a simple library for serializing and deserializing of bencoded data.


What is bencode?
----------------

Bencode is a data serialization format used by the popular 
[BitTorrent](http://bittorrent.org/) P2P file sharing system.
[Specification] (https://wiki.theory.org/BitTorrentSpecification#Bencoding) Bencode specification

It contains only four data types, namely:

- byte strings
- integers
- lists
- dictionaries


Mapping rules
-------------

 byte string    < -- > String
 
 integer        < -- > Integer
 
 list           < -- > ```java List<Object> ```
 
 dictionary     < -- > ```java SortedMap<String, Object> ```


Examples
--------

Serializing objects to stream as simple as calling `bencodeSerializer.write()` on them:

```java
    BencodeSerializer serializer = new BencodeSerializer(new FileOutputStream("example.ser"));
    serializer.write("foo bar");                 // => "7:foo bar"
    serializer.write(42);                        // => "i42e"
    serializer.write(Arrays.asList(1, 2, 3));    // => "li1ei2ei3ee"
    SortedMap<String, Object> map = new TreeMap<>();
    map.put("foo", 1);
    map.put("bar", -10);
    bencodeSerializer.write(map);                       // => "d3:bari-10e3:fooi1ee"
    serializer.close();
```

Decoding a complete data stream is as easy as calling `bencodeDeserializer.readElement()`.

Decoding a data stream in chunks works as follows:

```java
    BencodeDeserializer deserializer = new BencodeDeserializer(new FileInputStream("example.ser"));
    while (deserializer.hasNext()) {
        if (deserializer.hasNextDictionary()) {
            handleDictionary(deserializer.nextDictionary());
        } else if (deserializer.hasNextInt()) {
            handleInt(deserializer.nextInt());
        } else if (deserializer.hasNextString()) {
            handleString(deserializer.nextString());
        } else if (deserializer.hasNextList()) {
            handleList(deserializer.nextList());
        }
    }
    deserializer.close();
```

Java bencode binding
====================

This is a simple library for serializing and deserializing of bencoded data.


What is bencode?
----------------

Bencode is a data serialization format used by the popular 
[BitTorrent](http://bittorrent.org/) P2P file sharing system.

It contains only four data types, namely:

- byte strings
- integers
- lists
- dictionaries


Examples
--------

Serializing objects is as simple as calling `BencodeSerializer.write` on them:

```java
bencodeSerializer.write("foo bar");                 // => "7:foo bar"
bencodeSerializer.write(42);                        // => "i42e"
BList list = new BList();
// TODO // => "li1ei2ei3ee"
BDictionary map = new BDictionary();
// TODO // => "d3:bari-10e3:fooi1ee"
```

Decoding a complete data stream is as easy as calling `BencodeDeserializer.readElement()`.

Decoding a data stream in chunks works as follows:

```java
// TODO
```




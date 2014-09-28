package ru.yandex.test.task;

import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static ru.yandex.test.task.TestUtil.buildMap;

public class BencodeDeserilizerDictionaryTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testNextDictionaryStringInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("d3:bar4:spam3:fooi42ee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        if (!deserializer.hasNextDictionary()) {
            fail("Expected dictionary");
        }
        assertEquals(buildMap("bar", "spam", "foo", 42), deserializer.nextDictionary());
    }

    @Test
    public void testNextDictionarySorting() throws Exception {
        InputStream actual = new ByteArrayInputStream("d2:aa4:spam3:aaai42ee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextDictionary());
        assertEquals(buildMap("aa", "spam", "aaa", 42), deserializer.nextDictionary());
    }

    @Test
    public void testNextDictionaryOfListAndInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("d1:al2:el3:al1e3:fooi42ee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextDictionary());
        assertEquals(buildMap("a", Arrays.asList("el", "al1"),
                            "foo", 42),
                              deserializer.nextDictionary());
    }

    @Test
    public void testNextDictionaryOfDictionaryOfStringAndListOfStringAndInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("d1:dd2:d23:jone4:listl3:al1e3:inti42ee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextDictionary());
        assertEquals(buildMap("d", buildMap("d2", "jon"),
                            "list", Arrays.asList("al1"),
                            "int", 42),
                            deserializer.nextDictionary());
    }

    @Test
    public void testNext() throws Exception {
        InputStream actual = new ByteArrayInputStream("d2:d23:jonel3:al1ei42ee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        List expectedElements = Arrays.asList(buildMap("d2", "jon"), Arrays.asList("al1"), 42);
        List<Object> actualElements = new LinkedList<>();

        int elementsCount = 0;
        while (deserializer.hasNext()) {
            if (deserializer.hasNextDictionary()) {
                actualElements.add(deserializer.nextDictionary());
            } else if (deserializer.hasNextInt()) {
                actualElements.add(deserializer.nextInt());
            } else if (deserializer.hasNextString()) {
                actualElements.add(deserializer.nextString());
            } else if (deserializer.hasNextList()) {
                actualElements.add(deserializer.nextList());
            }
            elementsCount++;
        }
        assertEquals(3, elementsCount);
        assertEquals(expectedElements, actualElements);
    }

    @Test
    public void testNextDictionaryEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("de".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextDictionary());
        assertEquals(buildMap(), deserializer.nextDictionary());
    }

    @Test(expected = DeserializationException.class)
    public void testNextDictionaryIntegerKey() throws Exception {
        InputStream actual = new ByteArrayInputStream("di3e4:spame".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextDictionary();
    }

    @Test(expected = DeserializationException.class)
    public void testNextDictionaryWithoutValue() throws Exception {
        InputStream actual = new ByteArrayInputStream("d3:jon".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextDictionary();
    }

    @Test(expected = DeserializationException.class)
    public void testNextDictionaryWithoutPostfix() throws Exception {
        InputStream actual = new ByteArrayInputStream("d3:jon4:spam".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextDictionary();
    }

    @Test(expected = DeserializationException.class)
    public void testNextDictionaryOnlyPrefix() throws Exception {
        InputStream actual = new ByteArrayInputStream("d".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextDictionary();
    }

    @Test
    public void testHasNextListValid() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687ei8eel3:352i2eee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextList());
    }

    @Test
    public void testHasNextDictionaryList() throws Exception {
        InputStream actual = new ByteArrayInputStream("li687ei8ee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertFalse(deserializer.hasNextDictionary());
        assertTrue(deserializer.hasNextList());
        assertEquals(Arrays.asList(687, 8), deserializer.nextList());
    }

    @Test
    public void testHasNextDictionaryEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("de".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextDictionary());
    }
}
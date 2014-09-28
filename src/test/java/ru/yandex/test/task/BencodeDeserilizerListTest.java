package ru.yandex.test.task;

import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static ru.yandex.test.task.TestUtil.buildMap;

public class BencodeDeserilizerListTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testNextListInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("li5ee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        if (!deserializer.hasNextList()) {
            fail("Expected List");
        }
        List actualList = deserializer.nextList();
        assertEquals(Arrays.asList(5), actualList);
    }

    @Test
    public void testNextListStringAndInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("l5:55555i777ee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextList());
        assertEquals(Arrays.asList("55555", 777), deserializer.nextList());
    }

    @Test
    public void testNextListListInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687eee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextList());
        assertEquals(Arrays.asList(Arrays.asList(687)), deserializer.nextList());
    }

    @Test
    public void testNextListOfListIntsAndListOfStringAndInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687ei8eel3:352i2eee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextList());
        assertEquals(Arrays.asList(Arrays.asList(687, 8), Arrays.asList("352", 2)), deserializer.nextList());
    }

    @Test
    public void testNextListDictionary() throws Exception {
        InputStream actual = new ByteArrayInputStream("d3:inti687ee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertFalse(deserializer.hasNextList());
        assertTrue(deserializer.hasNextDictionary());
        Map<String, Object> map = deserializer.nextDictionary();
        assertEquals(1, map.size());
    }

    @Test(expected = DeserializationException.class)
    public void testNextListOfListIntAndListIntWithout2Postfixes() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687eli352e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextList();
    }

    @Test(expected = DeserializationException.class)
    public void testNextListOfListIntAndListIntWithout1Postfix() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687eli352ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextList();
    }

    @Test
    public void testNextListOfListIntAndInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687eei352ee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextList());
        assertEquals(Arrays.asList(Arrays.asList(687), 352), deserializer.nextList());
    }

    @Test(expected = DeserializationException.class)
    public void testNextListOfListIntAndIntWithoutPostfix() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687ei352ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextList();
    }

    @Test
    public void testNextListOfIntAndDictionaryOfInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("li352ed3:inti687eee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextList());
        List actualList = deserializer.nextList();
        Map<String, Object> expectedMap = buildMap("int", 687);
        assertEquals(Arrays.asList(352, expectedMap), actualList);
    }

    @Test
    public void testNextListEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("le".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextList());
        List actualList = deserializer.nextList();
        assertTrue(actualList.isEmpty());
    }

    @Test
    public void testHasNextListValid() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687ei8eel3:352i2eee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextList());
    }

    @Test
    public void testHasNextListDictionary() throws Exception {
        InputStream actual = new ByteArrayInputStream("d3:inti687ee".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertFalse(deserializer.hasNextList());
        assertTrue(deserializer.hasNextDictionary());
    }

    @Test
    public void testHasNextListEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("le".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextList());
    }
}
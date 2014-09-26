package ru.yandex.test.task;

import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.test.task.utils.BUtil.*;

public class BencodeDeserilizerListTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testReadElementListInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("li5ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isList(element));
        List<Object> list = getList(element);
        Integer integer = getInteger(list.get(0));
        assertEquals(new Integer(5), integer);
    }

    @Test
    public void testReadElementListString() throws Exception {
        InputStream actual = new ByteArrayInputStream("l5:55555e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isList(element));
        List<Object> list = getList(element);
        String string = getString(list.get(0));
        assertEquals("55555", string);
    }

    @Test
    public void testReadElementListListInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687eee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isList(element));
        List<Object> list = getList(element);
        List<Object> innerList = getList(list.get(0));
        assertEquals(new Integer(687), getInteger(innerList.get(0)));
    }

    @Test
    public void testReadElementListOfListIntAndListInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687eeli352eee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isList(element));
        List<Object> list = getList(element);
        List<Object> innerList1 = getList(list.get(0));
        List<Object> innerList2 = getList(list.get(1));
        assertEquals(new Integer(687), getInteger(innerList1.get(0)));
        assertEquals(new Integer(352), getInteger(innerList2.get(0)));
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementListOfListIntAndListIntWithout2Postfixes() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687eli352e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementListOfListIntAndListIntWithout1Postfix() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687eli352ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test
    public void testReadElementListOfListIntAndInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687eei352ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isList(element));
        List<Object> list = getList(element);
        List<Object> innerList = getList(list.get(0));
        assertTrue(isInteger(list.get(1)));

        Integer integer = getInteger(list.get(1));
        assertTrue(isInteger(innerList.get(0)));
        assertEquals(new Integer(687), getInteger(innerList.get(0)));
        assertEquals(new Integer(352), integer);
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementListOfListIntAndIntWithoutPostfix() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687ei352ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test
    public void testReadElementListOfIntAndDictionaryOfInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("li352ed3:inti687eee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isList(element));
        List<Object> list = getList(element);
        assertTrue(isInteger(list.get(0)));
        assertEquals(new Integer(352), getInteger(list.get(0)));

        assertTrue(isDictionary(list.get(1)));
        Map<String, Object> innerDictionary = getDictionary(list.get(1));
        assertEquals(new Integer(687), getInteger(innerDictionary.get("int")));
    }

    @Test
    public void testReadElementListEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("le".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isList(element));
        List<Object> list = getList(element);
        assertTrue(list.isEmpty());
    }
}
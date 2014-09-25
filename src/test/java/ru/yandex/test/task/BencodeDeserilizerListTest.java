package ru.yandex.test.task;

import org.junit.Ignore;
import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;
import ru.yandex.test.task.types.BInteger;
import ru.yandex.test.task.types.BString;
import ru.yandex.test.task.types.BValue;
import ru.yandex.test.task.util.BUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BencodeDeserilizerListTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testReadElementListInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("li5ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isList(element));
        List<BValue> list = BUtil.getList(element).getList();
        BInteger integer = BUtil.getInteger(list.get(0));
        assertEquals(new Integer(5), integer.getValue());
    }

    @Test
    public void testReadElementListString() throws Exception {
        InputStream actual = new ByteArrayInputStream("l5:55555e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isList(element));
        List<BValue> list = BUtil.getList(element).getList();
        BString string = BUtil.getString(list.get(0));
        assertEquals("55555", string.getValue());
    }

    @Test
    public void testReadElementListListInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687eee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isList(element));
        List<BValue> list = BUtil.getList(element).getList();
        List<BValue> innerList = BUtil.getList(list.get(0)).getList();
        assertEquals(new Integer(687), BUtil.getInteger(innerList.get(0)).getValue());
    }

    @Test
    public void testReadElementListOfListIntAndListInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687eeli352eee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isList(element));
        List<BValue> list = BUtil.getList(element).getList();
        List<BValue> innerList1 = BUtil.getList(list.get(0)).getList();
        List<BValue> innerList2 = BUtil.getList(list.get(1)).getList();
        assertEquals(new Integer(687), BUtil.getInteger(innerList1.get(0)).getValue());
        assertEquals(new Integer(352), BUtil.getInteger(innerList2.get(0)).getValue());
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
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isList(element));
        List<BValue> list = BUtil.getList(element).getList();
        List<BValue> innerList = BUtil.getList(list.get(0)).getList();
        assertTrue(BUtil.isInteger(list.get(1)));

        BInteger integer = BUtil.getInteger(list.get(1));
        assertTrue(BUtil.isInteger(innerList.get(0)));
        assertEquals(new Integer(687), BUtil.getInteger(innerList.get(0)).getValue());
        assertEquals(new Integer(352), integer.getValue());
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementListOfListIntAndIntWithoutPostfix() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687ei352ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Ignore
    @Test
    public void testReadElementListOfListIntAndDictionary() throws Exception {
        InputStream actual = new ByteArrayInputStream("lli687ei352ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isList(element));
        List<BValue> list = BUtil.getList(element).getList();
        List<BValue> innerList = BUtil.getList(list.get(0)).getList();
        assertTrue(BUtil.isInteger(list.get(1)));

        BInteger integer = BUtil.getInteger(list.get(1));
        assertTrue(BUtil.isInteger(innerList.get(0)));
        assertEquals(new Integer(687), BUtil.getInteger(innerList.get(0)).getValue());
        assertEquals(new Integer(352), integer.getValue());
    }

    @Test
    public void testReadElementListEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("le".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isList(element));
        List<BValue> list = BUtil.getList(element).getList();
        assertTrue(list.isEmpty());
    }
}
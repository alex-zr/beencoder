package ru.yandex.test.task.util;

import org.junit.Ignore;
import org.junit.Test;
import ru.yandex.test.task.BencodeDeserializer;
import ru.yandex.test.task.types.BValue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class BUtilTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testIsInteger() throws Exception {
        InputStream actual = new ByteArrayInputStream("i5e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isInteger(element));
    }

    @Test
    public void testIsString() throws Exception {
        InputStream actual = new ByteArrayInputStream("5:33344".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isString(element));
    }

    @Test
    public void testIsList() throws Exception {
        InputStream actual = new ByteArrayInputStream("li5ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isList(element));
    }

    @Ignore
    @Test
    public void testIsDictionary() throws Exception {
        fail();
    }

    @Ignore
    @Test
    public void testGetInteger() throws Exception {
        fail();
    }

    @Ignore
    @Test
    public void testGetDictionary() throws Exception {
        fail();
    }

    @Ignore
    @Test
    public void testGetString() throws Exception {
        fail();
    }

    @Ignore
    @Test
    public void testGetList() throws Exception {
        fail();
    }

    @Test
    public void testIsStringExcluding() throws Exception {
        InputStream actual = new ByteArrayInputStream("5:33344".getBytes());
        BencodeDeserializer deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isString(element));
        assertFalse(BUtil.isInteger(element));
    }

    @Test
    public void testIsIntExcluding() throws Exception {
        InputStream actual = new ByteArrayInputStream("i5e".getBytes());
        BencodeDeserializer deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isInteger(element));
        assertFalse(BUtil.isDictionary(element));
        assertFalse(BUtil.isList(element));
        assertFalse(BUtil.isString(element));
    }
}
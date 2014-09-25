package ru.yandex.test.task;

import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;
import ru.yandex.test.task.types.BValueWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class BencodeDeserilizerStringTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testReadElementString() throws Exception {
        InputStream actual = new ByteArrayInputStream("5:33344".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isString());
        assertEquals("33344", element.getString().getValue());
    }

    @Test
    public void testReadElementStringSingle() throws Exception {
        InputStream actual = new ByteArrayInputStream("1:&".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isString());
        assertEquals("&", element.getString().getValue());
    }

    @Test
    public void testReadElementStringTwoSinglerComplex() throws Exception {
        InputStream actual = new ByteArrayInputStream("1:&3:JON".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isString());
        assertEquals("&", element.getString().getValue());

        element = deserializer.readElement();
        assertTrue(element.isString());
        assertEquals("JON", element.getString().getValue());
    }

    @Test(expected=IOException.class)
    public void testReadElementEmptyStream() throws Exception {
        InputStream actual = new ByteArrayInputStream("".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test
    public void testReadElementStringEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("0:".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isString());
        assertEquals("", element.getString().getValue());
    }

    @Test(expected=DeserializationException.class)
    public void testReadElementStringEmptyLength() throws Exception {
        InputStream actual = new ByteArrayInputStream(":e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isString());
        assertEquals("33344", element.getString().getValue());
    }

    @Test(expected=DeserializationException.class)
    public void testReadElementStringEmptyContent() throws Exception {
        InputStream actual = new ByteArrayInputStream("2:".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isString());
        assertEquals("33344", element.getString().getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testReadElementStringNull() throws Exception {
        InputStream actual = new ByteArrayInputStream(null);
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test
    public void testReadElementStringFromComplex() throws Exception {
        InputStream actual = new ByteArrayInputStream("1:a4:!@#$".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isString());
        assertEquals("a", element.getString().getValue());

        element = deserializer.readElement();
        assertTrue(element.isString());
        assertEquals("!@#$", element.getString().getValue());
    }

    @Test
    public void testIsStringExcluding() throws Exception {
        InputStream actual = new ByteArrayInputStream("5:33344".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isString());
        assertFalse(element.isInteger());
        assertFalse(element.isList());
        assertFalse(element.isDictionary());
    }
}
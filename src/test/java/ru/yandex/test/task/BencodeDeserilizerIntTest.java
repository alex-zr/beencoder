package ru.yandex.test.task;

import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;
import ru.yandex.test.task.types.BValueWrapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class BencodeDeserilizerIntTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testReadElementInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("i5e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isInteger());
        assertEquals(5, element.getInteger().getValue());
    }

    @Test
    public void testReadElementIntNegative() throws Exception {
        InputStream actual = new ByteArrayInputStream("i-200e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isInteger());
        assertEquals(-200, element.getInteger().getValue());
    }

    @Test
    public void testReadElementIntFromComplex() throws Exception {
        InputStream actual = new ByteArrayInputStream("i-200ei0e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isInteger());
        assertEquals(-200, element.getInteger().getValue());

        element = deserializer.readElement();
        assertTrue(element.isInteger());
        assertEquals(0, element.getInteger().getValue());

    }

    @Test(expected = DeserializationException.class)
    public void testReadElementIntIllegal() throws Exception {
        InputStream actual = new ByteArrayInputStream("iRe".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementIntEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("ie".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test
    public void testIsIntExcluding() throws Exception {
        InputStream actual = new ByteArrayInputStream("i5e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isInteger());
        assertFalse(element.isDictionary());
        assertFalse(element.isList());
        assertFalse(element.isString());
    }
}
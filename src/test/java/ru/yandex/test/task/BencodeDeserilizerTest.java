package ru.yandex.test.task;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;
import ru.yandex.test.task.types.BValueWrapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BencodeDeserilizerTest {
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
        InputStream actual = new ByteArrayInputStream("i-200e4grow".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValueWrapper element = deserializer.readElement();

        assertTrue(element.isInteger());
        assertEquals(-200, element.getInteger().getValue());
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
}
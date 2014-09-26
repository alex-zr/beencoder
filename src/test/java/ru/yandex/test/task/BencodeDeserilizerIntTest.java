package ru.yandex.test.task;

import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.test.task.utils.BUtil.getInteger;
import static ru.yandex.test.task.utils.BUtil.isInteger;

public class BencodeDeserilizerIntTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testReadElementInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("i5e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isInteger(element));
        assertEquals(new Integer(5), getInteger(element));
    }

    @Test
    public void testReadElementIntNegative() throws Exception {
        InputStream actual = new ByteArrayInputStream("i-200e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isInteger(element));
        assertEquals(new Integer(-200), getInteger(element));
    }

    @Test
    public void testReadElementIntFromComplex() throws Exception {
        InputStream actual = new ByteArrayInputStream("i-200ei0e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isInteger(element));
        assertEquals(new Integer(-200), getInteger(element));

        element = deserializer.readElement();
        assertTrue(isInteger(element));
        assertEquals(new Integer(0), getInteger(element));
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementIntIllegalValue() throws Exception {
        InputStream actual = new ByteArrayInputStream("iRe".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementIntWithoutPostfix() throws Exception {
        InputStream actual = new ByteArrayInputStream("i55".getBytes());
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
package ru.yandex.test.task;

import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;
import ru.yandex.test.task.types.BValue;
import ru.yandex.test.task.util.BUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BencodeDeserilizerIntTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testReadElementInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("i5e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isInteger(element));
        assertEquals(new Integer(5), BUtil.getInteger(element).getValue());
    }

    @Test
    public void testReadElementIntNegative() throws Exception {
        InputStream actual = new ByteArrayInputStream("i-200e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isInteger(element));
        assertEquals(new Integer(-200), BUtil.getInteger(element).getValue());
    }

    @Test
    public void testReadElementIntFromComplex() throws Exception {
        InputStream actual = new ByteArrayInputStream("i-200ei0e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        BValue element = deserializer.readElement();

        assertTrue(BUtil.isInteger(element));
        assertEquals(new Integer(-200), BUtil.getInteger(element).getValue());

        element = deserializer.readElement();
        assertTrue(BUtil.isInteger(element));
        assertEquals(new Integer(0), BUtil.getInteger(element).getValue());

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
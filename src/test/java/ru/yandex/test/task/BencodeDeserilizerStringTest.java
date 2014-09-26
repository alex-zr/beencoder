package ru.yandex.test.task;

import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.test.task.utils.BUtil.getString;
import static ru.yandex.test.task.utils.BUtil.isString;

public class BencodeDeserilizerStringTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testReadElementString() throws Exception {
        InputStream actual = new ByteArrayInputStream("5:33344".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isString(element));
        assertEquals("33344", getString(element));
    }

    @Test
    public void testReadElementStringSingle() throws Exception {
        InputStream actual = new ByteArrayInputStream("1:&".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isString(element));
        assertEquals("&", getString(element));
    }

    @Test
    public void testReadElementStringTwoSinglerComplex() throws Exception {
        InputStream actual = new ByteArrayInputStream("1:&3:JON".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isString(element));
        assertEquals("&", getString(element));


        element = deserializer.readElement();
        assertTrue(isString(element));
        assertEquals("JON", getString(element));
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementStringWrongDelimiter() throws Exception {
        InputStream actual = new ByteArrayInputStream("1;&3:JON".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isString(element));
        assertEquals("&", getString(element));


        element = deserializer.readElement();
        assertTrue(isString(element));
        assertEquals("JON", getString(element));
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementStringIllegalLength() throws Exception {
        InputStream actual = new ByteArrayInputStream("1:%&3:JON".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isString(element));
        assertEquals("%", getString(element));

        deserializer.readElement();
    }

    @Test(expected=DeserializationException.class)
    public void testReadElementEmptyStream() throws Exception {
        InputStream actual = new ByteArrayInputStream("".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test
    public void testReadElementStringEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("0:".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isString(element));
        assertEquals("", getString(element));
    }

    @Test(expected=DeserializationException.class)
    public void testReadElementStringEmptyLength() throws Exception {
        InputStream actual = new ByteArrayInputStream(":e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test(expected=DeserializationException.class)
    public void testReadElementStringEmptyContent() throws Exception {
        InputStream actual = new ByteArrayInputStream("2:".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
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
        Object element = deserializer.readElement();

        assertTrue(isString(element));
        assertEquals("a", getString(element));

        element = deserializer.readElement();

        assertTrue(isString(element));
        assertEquals("!@#$", getString(element));
    }
}

package ru.yandex.test.task;

import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class BencodeDeserilizerStringTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testNextStringString() throws Exception {
        InputStream actual = new ByteArrayInputStream("5:33344".getBytes());
        deserializer = new BencodeDeserializer(actual);

        if (!deserializer.hasNextString()) {
            fail("Expected String");
        }
        
        String stringElement = deserializer.nextString();
        assertEquals("33344", stringElement);
    }

    @Test
    public void testNextStringStringSingle() throws Exception {
        InputStream actual = new ByteArrayInputStream("1:&".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextString());
        assertEquals("&", deserializer.nextString());
    }

    @Test
    public void testNextStringStringAndStringAndString() throws Exception {
        InputStream actual = new ByteArrayInputStream("1:&3:JON5:_:_;.".getBytes());
        deserializer = new BencodeDeserializer(actual);

        String[] expectedValues = {"&", "JON", "_:_;."};
        int expectedIndex = 0;

        while (deserializer.hasNextString()) {
            String actualIntElement = deserializer.nextString();
            assertEquals(expectedValues[expectedIndex++], actualIntElement);
        }

        assertEquals(expectedValues.length, expectedIndex);
    }

    @Test(expected = DeserializationException.class)
    public void testNextStringStringWrongDelimiter() throws Exception {
        InputStream actual = new ByteArrayInputStream("1;&3:JON".getBytes());
        deserializer = new BencodeDeserializer(actual);

        String stringElement = deserializer.nextString();
        assertEquals("&", stringElement);

        deserializer.nextString();
    }

    @Test(expected = DeserializationException.class)
    public void testNextStringStringIllegalLength() throws Exception {
        InputStream actual = new ByteArrayInputStream("1:%&3:JON".getBytes());
        deserializer = new BencodeDeserializer(actual);

        String stringElement = deserializer.nextString();

        assertEquals("%", stringElement);

        deserializer.nextString();
    }

    @Test(expected=DeserializationException.class)
    public void testNextStringEmptyStream() throws Exception {
        InputStream actual = new ByteArrayInputStream("".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextString();
    }

    @Test
    public void testNextStringStringEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("0:".getBytes());
        deserializer = new BencodeDeserializer(actual);
        String stringElement = deserializer.nextString();

        assertEquals("", stringElement);
    }

    @Test(expected=DeserializationException.class)
    public void testNextStringStringEmptyLength() throws Exception {
        InputStream actual = new ByteArrayInputStream(":e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextString();
    }

    @Test(expected=DeserializationException.class)
    public void testNextStringStringEmptyContent() throws Exception {
        InputStream actual = new ByteArrayInputStream("2:".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextString();
    }

    @Test(expected = NullPointerException.class)
    public void testNextStringStringNull() throws Exception {
        InputStream actual = new ByteArrayInputStream(null);
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextString();
    }

    @Test
    public void testNextStringStringFromComplex() throws Exception {
        InputStream actual = new ByteArrayInputStream("1:a4:!@#$".getBytes());
        deserializer = new BencodeDeserializer(actual);

        String stringElement = deserializer.nextString();
        assertEquals("a", stringElement);

        stringElement = deserializer.nextString();
        assertEquals("!@#$", stringElement);
    }

    @Test
    public void testHasNextStringValid() throws Exception {
        InputStream actual = new ByteArrayInputStream("2:22".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextString());
    }

    @Test
    public void testHasNextStringInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("i54e".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertFalse(deserializer.hasNextString());
        assertTrue(deserializer.hasNextInt());
        assertEquals(54, deserializer.nextInt());
    }

    @Test
    public void testHasNextStringEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("0:".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextString());
    }
}

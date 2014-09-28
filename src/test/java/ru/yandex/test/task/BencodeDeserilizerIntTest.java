package ru.yandex.test.task;

import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class BencodeDeserilizerIntTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testNextInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("i5e".getBytes());
        deserializer = new BencodeDeserializer(actual);

        if (!deserializer.hasNextInt()) {
            fail("Expected number");
        }

        int intElement = deserializer.nextInt();
        assertEquals(5, intElement);
    }

    @Test
    public void testNextIntNegative() throws Exception {
        InputStream actual = new ByteArrayInputStream("i-200e".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextInt());
        assertEquals(-200, deserializer.nextInt());
    }

    @Test
    public void testNextIntAndInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("i-200ei0e".getBytes());
        deserializer = new BencodeDeserializer(actual);
        int[] expectedValues = {-200, 0};
        int expectedIndex = 0;

        while (deserializer.hasNextInt()) {
            int actualIntElement = deserializer.nextInt();
            assertEquals(expectedValues[expectedIndex++], actualIntElement);
        }

        assertEquals(expectedValues.length, expectedIndex);
    }

    @Test(expected = DeserializationException.class)
    public void testNextIntIllegalValue() throws Exception {
        InputStream actual = new ByteArrayInputStream("iRe".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextInt();
    }

    @Test(expected = DeserializationException.class)
    public void testNextIntWithoutPostfix() throws Exception {
        InputStream actual = new ByteArrayInputStream("i55".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextInt();
    }

    @Test(expected = DeserializationException.class)
    public void testNextIntEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("ie".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.nextInt();
    }

    @Test
    public void testHasNextIntValid() throws Exception {
        InputStream actual = new ByteArrayInputStream("i5e".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextInt());
    }

    @Test
    public void testHasNextIntAndString() throws Exception {
        InputStream actual = new ByteArrayInputStream("i5e3:jon".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertTrue(deserializer.hasNextInt());
        assertEquals(5, deserializer.nextInt());
        assertTrue(deserializer.hasNextString());
        assertEquals("jon", deserializer.nextString());
    }

    @Test
    public void testHasNextIntString() throws Exception {
        InputStream actual = new ByteArrayInputStream("3:qwe".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertFalse(deserializer.hasNextInt());
        assertTrue(deserializer.hasNextString());
        assertEquals("qwe", deserializer.nextString());
    }

    @Test
    public void testHasNextIntEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("ie".getBytes());
        deserializer = new BencodeDeserializer(actual);

        assertFalse(deserializer.hasNextInt());
    }
}
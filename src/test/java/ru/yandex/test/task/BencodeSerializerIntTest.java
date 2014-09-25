package ru.yandex.test.task;

import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

public class BencodeSerializerIntTest {
    private BencodeSerializer serializer;

    @Test
    public void testWriteInt() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        serializer.write(223);

        assertEquals("i223e", actual.toString());
    }

    @Test
    public void testWriteIntNegative() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        serializer.write(-3);

        assertEquals("i-3e", actual.toString());
    }

    @Test
    public void testWriteIntZero() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        serializer.write(-0);

        assertEquals("i0e", actual.toString());
    }

    @Test
    public void testWriteIntTwice() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        serializer.write(0);
        serializer.write(-20);

        assertEquals("i0ei-20e", actual.toString());
    }

    @Test
    public void testFlushClose() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        serializer.write(0);
        serializer.flush();
        serializer.write(-20);
        serializer.close();

        assertEquals("i0ei-20e", actual.toString());
    }
}
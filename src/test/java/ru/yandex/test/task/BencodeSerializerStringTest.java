package ru.yandex.test.task;

import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

public class BencodeSerializerStringTest {
    private BencodeSerializer serializer;

    @Test
    public void testWriteString() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        serializer.write("Cypher");

        assertEquals("6:Cypher", actual.toString());
    }

    @Test(expected=NullPointerException.class)
    public void testWriteStringNull() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        serializer.write((String)null);
    }

    @Test
    public void testWriteStringEmpty() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        serializer.write("");

        assertEquals("0:", actual.toString());
    }

    @Test
    public void testWriteStringTwice() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        serializer.write("0");
        serializer.write("");

        assertEquals("1:00:", actual.toString());
    }
}
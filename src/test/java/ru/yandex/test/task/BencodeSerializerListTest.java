package ru.yandex.test.task;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class BencodeSerializerListTest {
    private BencodeSerializer serializer;

    @Test
    public void testWriteListOfInt() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        List<Object> list = new LinkedList<>();
        list.add(12);
        serializer.write(list);

        assertEquals("li12ee", actual.toString());
    }

    @Test
    public void testWriteListOfIntAndListInt() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        List<Object> list = new LinkedList<>();
        List<Object> innerList1 = new LinkedList<>();
        innerList1.add(12);
        list.add(33455);
        list.add(innerList1);
        serializer.write(list);

        assertEquals("li33455eli12eee", actual.toString());
    }

    @Test
    public void testWriteListOfListIntAndListInt() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        List<Object> list = new LinkedList<>();

        List<Object> innerList1 = new LinkedList<>();
        innerList1.add(12);
        List<Object> innerList2 = new LinkedList<>();
        innerList2.add(673);
        list.add(innerList1);
        list.add(innerList2);
        serializer.write(list);

        assertEquals("lli12eeli673eee", actual.toString());
    }

    @Test
    public void testWriteListEmpty() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        List<Object> list = new LinkedList<>();
        serializer.write(list);

        assertEquals("le", actual.toString());
    }


    @Test
    public void testWriteListOfIntAndDictionary() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        List<Object> list = new LinkedList<>();
        Map<String, Object> innerDictionary = new TreeMap<>();
        innerDictionary.put("i", 12);
        list.add(33455);
        list.add(innerDictionary);
        serializer.write(list);

        assertEquals("li33455ed1:ii12eee", actual.toString());

    }
}
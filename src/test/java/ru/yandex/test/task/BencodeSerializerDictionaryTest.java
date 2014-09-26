package ru.yandex.test.task;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class BencodeSerializerDictionaryTest {
    private BencodeSerializer serializer;

    @Test
    public void testWriteDictionaryWithStringAndInt() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        Map<String, Object> dictionary = new TreeMap<>();
        dictionary.put("bar", "spam");
        dictionary.put("foo", 42);

        serializer.write(dictionary);

        assertEquals("d3:bar4:spam3:fooi42ee", actual.toString());
    }

    @Test
    public void testWriteDictionaryOfStringIntAndDictionaryOfStringInt() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        Map<String, Object> firstDictionary = new TreeMap<>();
        firstDictionary.put("bag", "spa");
        firstDictionary.put("fool", 41);

        Map<String, Object> secondDictionary = new TreeMap<>();
        secondDictionary.put("bar", "spam");
        secondDictionary.put("foo", 42);


        serializer.write(firstDictionary);
        serializer.write(secondDictionary);

        assertEquals("d3:bag3:spa4:fooli41eed3:bar4:spam3:fooi42ee", actual.toString());
    }

    @Test
    public void testWriteDictionaryOfDictionaryOfIntAndString() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        Map<String, Object> dictionary = new TreeMap<>();

        Map<String, Object> innerDictionary = new TreeMap<>();
        innerDictionary.put("int", 246);

        dictionary.put("dinner", innerDictionary);
        dictionary.put("str", "val");
        serializer.write(dictionary);

        assertEquals("d6:dinnerd3:inti246ee3:str3:vale", actual.toString());
    }

    @Test
    public void testWriteDictionaryOfListOfDictionaryOfIntStringAndDictionaryOfInt() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        Map<String, Object> dictionary = new TreeMap<>();

        List<Object> innerList = new LinkedList<>();
        Map<String, Object> innerDictionary = new TreeMap<>();
        innerList.add(innerDictionary);
        innerDictionary.put("int", 77);

        dictionary.put("linner", innerList);
        dictionary.put("str", "val");

        Map<String, Object> secondDictionary = new TreeMap<>();
        secondDictionary.put("int", 266);
        dictionary.put("senior", secondDictionary);
        serializer.write(dictionary);

        assertEquals("d6:linnerld3:inti77eee6:seniord3:inti266ee3:str3:vale", actual.toString());
    }

    @Test
    public void testWriteDictionaryEmpty() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        Map<String, Object> dictionary = new TreeMap<>();

        serializer.write(dictionary);

        assertEquals("de", actual.toString());
    }

    @Test
    public void testWriteDictionaryOfEmptyDictionary() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        Map<String, Object> dictionary = new TreeMap<>();
        Map<String, Object> innerDictionary = new TreeMap<>();
        dictionary.put("1", innerDictionary);


        serializer.write(dictionary);

        assertEquals("d1:1dee", actual.toString());
    }

    @Test
    public void testWriteDictionaryOfDictionaryWithEmptyKey() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        Map<String, Object> dictionary = new TreeMap<>();
        Map<String, Object> innerDictionary = new TreeMap<>();
        dictionary.put("", innerDictionary);

        serializer.write(dictionary);

        assertEquals("d0:dee", actual.toString());
    }
}
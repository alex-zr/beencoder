package ru.yandex.test.task;

import org.junit.Test;
import ru.yandex.test.task.exceptions.DeserializationException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.test.task.utils.BUtil.*;

public class BencodeDeserilizerDictionaryTest {
    private BencodeDeserializer deserializer;

    @Test
    public void testReadElementDictionaryStringInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("d3:bar4:spam3:fooi42ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isDictionary(element));
        Map<String, Object> map = getDictionary(element);
        Iterator<Map.Entry<String, Object>> entryIterator = map.entrySet().iterator();
        Map.Entry<String, Object> firstEntry = entryIterator.next();
        Map.Entry<String, Object> secondEntry = entryIterator.next();
        String firstEntryKey = firstEntry.getKey();
        Object firstEntryValue = firstEntry.getValue();
        String secondEntryKey = secondEntry.getKey();
        Object secondEntryValue = secondEntry.getValue();

        String firstStringKey = getString(firstEntryKey);
        String firstStringValue = getString(firstEntryValue);
        String secondStringKey = getString(secondEntryKey);
        Integer secondIntegerValue = getInteger(secondEntryValue);
        assertEquals("bar", firstStringKey);
        assertEquals("spam", firstStringValue);
        assertEquals("foo", secondStringKey);
        assertEquals(new Integer(42), secondIntegerValue);
    }

    @Test
    public void testReadElementDictionarySorting() throws Exception {
        InputStream actual = new ByteArrayInputStream("d2:aa4:spam3:aaai42ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isDictionary(element));
        Map<String, Object> map = getDictionary(element);
        Iterator<Map.Entry<String, Object>> entryIterator = map.entrySet().iterator();
        Map.Entry<String, Object> firstEntry = entryIterator.next();
        Map.Entry<String, Object> secondEntry = entryIterator.next();
        String firstEntryKey = firstEntry.getKey();
        String secondEntryKey = secondEntry.getKey();

        String firstStringKey = getString(firstEntryKey);
        String secondStringKey = getString(secondEntryKey);
        assertEquals("aa", firstStringKey);
        assertEquals("aaa", secondStringKey);
    }

    @Test
    public void testReadElementDictionaryOfListAndInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("d1:al2:el3:al1e3:fooi42ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isDictionary(element));
        Map<String, Object> map = getDictionary(element);
        Iterator<Map.Entry<String, Object>> entryIterator = map.entrySet().iterator();
        Map.Entry<String, Object> firstEntry = entryIterator.next();
        Map.Entry<String, Object> secondEntry = entryIterator.next();
        String firstEntryKey = firstEntry.getKey();
        Object firstEntryValue = firstEntry.getValue();
        String secondEntryKey = secondEntry.getKey();
        Object secondEntryValue = secondEntry.getValue();

        String firstStringKey = getString(firstEntryKey);
        List<Object> firstListValue = getList(firstEntryValue);
        String firstListStringValue1 = getString(firstListValue.get(0));
        String firstListStringValue2 = getString(firstListValue.get(1));

        String secondStringKey = getString(secondEntryKey);
        Integer secondIntegerValue = getInteger(secondEntryValue);

        assertEquals("a", firstStringKey);
        assertEquals(2, firstListValue.size());
        assertEquals("el", firstListStringValue1);
        assertEquals("al1", firstListStringValue2);
        assertEquals("foo", secondStringKey);
        assertEquals(new Integer(42), secondIntegerValue);
    }

    @Test
    public void testReadElementDictionaryOfDictionaryOfStringAndListOfStringAndInt() throws Exception {
        InputStream actual = new ByteArrayInputStream("d1:dd2:d23:jone4:listl3:al1e3:inti42ee".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isDictionary(element));
        Map<String, Object> map = getDictionary(element);

        Iterator<Map.Entry<String, Object>> entryIterator = map.entrySet().iterator();
        Map.Entry<String, Object> dictionaryEntry = entryIterator.next();
        Map.Entry<String, Object> integerEntry = entryIterator.next();
        Map.Entry<String, Object> listEntry = entryIterator.next();
        String dictionaryEntryKey = dictionaryEntry.getKey();
        Object dictionaryEntryValue = dictionaryEntry.getValue();
        assertEquals("d", dictionaryEntryKey);

        Map<String, Object> innerDictionary = getDictionary(dictionaryEntryValue);
        Map.Entry<String, Object> innerDicEntry = innerDictionary.entrySet().iterator().next();
        String innerDicKey = innerDicEntry.getKey();
        String innerDicStringValue = (String)innerDicEntry.getValue();
        assertEquals("d2", innerDicKey);
        assertEquals("jon", innerDicStringValue);

        List<Object> innerList = getList(listEntry.getValue());
        String innerListStringValue = getString(innerList.get(0));
        assertEquals(1, innerList.size());
        assertEquals("al1", innerListStringValue);


        Integer innerIntegerValue = getInteger(integerEntry.getValue());
        assertEquals(new Integer(42), innerIntegerValue);
    }

    @Test
    public void testReadElementDictionaryEmpty() throws Exception {
        InputStream actual = new ByteArrayInputStream("de".getBytes());
        deserializer = new BencodeDeserializer(actual);
        Object element = deserializer.readElement();

        assertTrue(isDictionary(element));
        Map<String, Object> map = getDictionary(element);
        assertEquals(0, map.entrySet().size());
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementDictionaryIntegerKey() throws Exception {
        InputStream actual = new ByteArrayInputStream("di3e4:spame".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementDictionaryWithoutValue() throws Exception {
        InputStream actual = new ByteArrayInputStream("d3:jon".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementDictionaryWithoutPostfix() throws Exception {
        InputStream actual = new ByteArrayInputStream("d3:jon4:spam".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }

    @Test(expected = DeserializationException.class)
    public void testReadElementDictionaryOnlyPrefix() throws Exception {
        InputStream actual = new ByteArrayInputStream("d".getBytes());
        deserializer = new BencodeDeserializer(actual);
        deserializer.readElement();
    }
}
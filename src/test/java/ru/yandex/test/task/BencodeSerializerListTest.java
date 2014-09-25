package ru.yandex.test.task;

import org.junit.Ignore;
import org.junit.Test;
import ru.yandex.test.task.types.BInteger;
import ru.yandex.test.task.types.BList;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

public class BencodeSerializerListTest {
    private BencodeSerializer serializer;

    @Test
    public void testWriteListInt() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        BList list = new BList();
        list.add(new BInteger(12));
        serializer.write(list);

        assertEquals("li12ee", actual.toString());
    }

    @Test
    public void testWriteListOfIntAndListInt() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        BList list = new BList();
        BList innerList1 = new BList();
        innerList1.add(new BInteger(12));
        list.add(new BInteger(33455));
        list.add(innerList1);
        serializer.write(list);

        assertEquals("li33455eli12eee", actual.toString());
    }

    @Test
    public void testWriteListOfListIntAndListInt() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        BList list = new BList();
        BList innerList1 = new BList();
        innerList1.add(new BInteger(12));
        BList innerList2 = new BList();
        innerList2.add(new BInteger(673));
        list.add(innerList1);
        list.add(innerList2);
        serializer.write(list);

        assertEquals("lli12eeli673eee", actual.toString());
    }

    @Test
    public void testWriteListEmpty() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        BList list = new BList();
        serializer.write(list);

        assertEquals("le", actual.toString());
    }

    @Ignore
    @Test
    public void testWriteListOfIntAndDictionary() throws Exception {
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        serializer = new BencodeSerializer(actual);
        BList list = new BList();
        serializer.write(list);

        assertEquals("le", actual.toString());
    }
}
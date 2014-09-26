package ru.yandex.test.task.utils;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class BUtilTest {

    @Test
    public void testIsIntegerValid() throws Exception {
        assertTrue(BUtil.isInteger(-200));
    }

    @Test
    public void testIsIntegerNull() throws Exception {
        assertFalse(BUtil.isInteger(null));
    }

    @Test
    public void testIsIntegerObject() throws Exception {
        assertFalse(BUtil.isInteger(new Object()));
    }

    @Test
    public void testIsStringValid() throws Exception {
        assertTrue(BUtil.isString("ok"));
    }

    @Test
    public void testIsStringEmpty() throws Exception {
        assertTrue(BUtil.isString(""));
    }

    @Test
    public void testIsStringNull() throws Exception {
        assertFalse(BUtil.isString(null));
    }

    @Test
    public void testIsStringIllegal() throws Exception {
        assertFalse(BUtil.isString(new StringBuilder()));
    }

    @Test
    public void testIsListArrayList() throws Exception {
        assertTrue(BUtil.isList(new ArrayList<>()));
    }

    @Test
    public void testIsListLinkedList() throws Exception {
        assertTrue(BUtil.isList(new LinkedList<>()));
    }

    @Test
    public void testIsListNull() throws Exception {
        assertFalse(BUtil.isList(null));
    }

    @Test
    public void testIsListIllegalArrayDeque() throws Exception {
        assertFalse(BUtil.isList(new ArrayDeque<>()));
    }

    @Test
    public void testIsDictionaryTreeMap() throws Exception {
        assertTrue(BUtil.isDictionary(new TreeMap<>()));
    }

    @Test
    public void testIsDictionaryHashMap() throws Exception {
        assertFalse(BUtil.isDictionary(new HashMap<>()));
    }

    @Test
    public void testIsDictionaryIdentityHashMap() throws Exception {
        assertFalse(BUtil.isDictionary(new IdentityHashMap<>()));
    }

    @Test
    public void testIsDictionaryIllegalNull() throws Exception {
        assertFalse(BUtil.isDictionary(null));
    }

    @Test
    public void testIsDictionaryIllegalThread() throws Exception {
        assertFalse(BUtil.isDictionary(new Thread()));
    }

    @Test
    public void testGetIntegerValid() throws Exception {
        assertEquals(new Integer(12), BUtil.getInteger(12));
    }

    @Test
    public void testGetIntegerValidNegative() throws Exception {
        assertEquals(new Integer(-12), BUtil.getInteger(-12));
    }

    @Test
    public void testGetIntegerValidZero() throws Exception {
        assertEquals(new Integer(0), BUtil.getInteger(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIntegerIllegalNull() throws Exception {
        BUtil.getInteger(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIntegerIllegalDouble() throws Exception {
        BUtil.getInteger(100.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIntegerIllegalObject() throws Exception {
        BUtil.getInteger(new Object());
    }

    @Test
    public void testGetStringValid() throws Exception {
        assertEquals("5e$", BUtil.getString("5e$"));
    }

    @Test
    public void testGetStringValidEmpty() throws Exception {
        assertEquals("", BUtil.getString(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringIllegalNull() throws Exception {
        BUtil.getString(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringIllegalStringBuffer() throws Exception {
        BUtil.getString(new StringBuffer());
    }

    @Test
    public void testGetListValidLinkedList() throws Exception {
        assertEquals(new LinkedList<>(), BUtil.getList(new LinkedList<>()));
    }

    @Test
    public void testGetListValidArrayList() throws Exception {
        assertEquals(new ArrayList<>(), BUtil.getList(new ArrayList<>()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetListIllegalNull() throws Exception {
        BUtil.getList(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetListIllegalPriorityQueue() throws Exception {
        BUtil.getList(new PriorityQueue<>());
    }

    @Test
    public void testGetDictionaryValid() throws Exception {
        assertEquals(new TreeMap<String, Object>(), BUtil.getDictionary(new TreeMap<>()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDictionaryIllegalHashMap() throws Exception {
        BUtil.getDictionary(new HashMap<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDictionaryIllegalTreeSet() throws Exception {
        BUtil.getDictionary(new TreeSet<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDictionaryIllegalNull() throws Exception {
        BUtil.getDictionary(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDictionaryIllegalException() throws Exception {
        BUtil.getDictionary(new Exception());
    }
}
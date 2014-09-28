package ru.yandex.test.task.utils;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}
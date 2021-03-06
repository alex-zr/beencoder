package ru.yandex.test.task.utils;

import java.util.List;
import java.util.SortedMap;

/**
 * Supplementary util for after "B-encode" deserialization purposes
 *
 * @author Oleksandr Roshchupkin
 * @since 26-09-2014
 */
public class BUtil {
    public static final char INT_PREFIX = 'i';
    public static final char DELIMITER = ':';
    public static final char LIST_PREFIX = 'l';
    public static final char DICTIONARY_PREFIX = 'd';
    public static final char POSTFIX = 'e';

    /**
     * Check if value is Integer
     *
     * @param value - value to test
     * @return is true if value is Integer
     */
    public static boolean isInteger(Object value) {
        return value instanceof Integer;
    }

    /**
     * Check if value is String
     *
     * @param value - value to test
     * @return is true if value is String
     */
    public static boolean isString(Object value) {
        return value instanceof String;
    }

    /**
     * Check if value is List
     *
     * @param value - value to test
     * @return is true if value is List
     */
    public static boolean isList(Object value) {
        return value instanceof List;
    }

    /**
     * Check if value is SortedMap
     *
     * @param value - value to test
     * @return is true if value is SortedMap
     */
    public static boolean isDictionary(Object value) {
        return value instanceof SortedMap;
    }
}

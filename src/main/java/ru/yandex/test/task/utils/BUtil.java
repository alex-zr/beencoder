package ru.yandex.test.task.utils;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created with IntelliJ IDEA.
 * User: al1
 * Date: 26.09.14
 */
public class BUtil {
    public static boolean isInteger(Object value) {
        return value instanceof Integer;
    }

    public static boolean isString(Object value) {
        return value instanceof String;
    }

    public static boolean isList(Object value) {
        return value instanceof List;
    }

    public static boolean isDictionary(Object value) {
        return value instanceof SortedMap;
    }

    public static Integer getInteger(Object value) {
        if (!isInteger(value)) {
            throw new IllegalStateException("Type is not an int");
        }
        return  ((Integer) value);
    }


    public static Map<String, Object> getDictionary(Object value) {
        if (!isDictionary(value)) {
            throw new IllegalStateException("Type is not an Dictionary");
        }
        return ((Map<String, Object>) value);
    }

    public static String getString(Object value) {
        if (!isString(value)) {
            throw new IllegalStateException("Type is not an String");
        }
        return ((String) value);

    }

    public static List<Object> getList(Object value) {
        if (!isList(value)) {
            throw new IllegalStateException("Type is not an List");
        }
        return ((List<Object>) value);

    }
}

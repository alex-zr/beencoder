package ru.yandex.test.task;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: al1
 * Date: 28.09.14
 */
public class TestUtil {
    public static Map<String, Object> buildMap(Object ... keyValues) {
        assertTrue(keyValues.length % 2 == 0);
        Map<String, Object> map = new LinkedHashMap<>(keyValues.length / 2);
        for (int i = 0; i < keyValues.length; i++) {
            if (i % 2 == 0) {
                assertTrue(keyValues[i] instanceof String);
                map.put((String)keyValues[i], null);
            } else {
                map.put((String)keyValues[i - 1], keyValues[i]);
            }
        }
        return map;
    }
}

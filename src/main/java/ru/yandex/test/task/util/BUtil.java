package ru.yandex.test.task.util;

import ru.yandex.test.task.types.*;

/**
 * Created with IntelliJ IDEA.
 * User: al1
 * Date: 25.09.14
 */
public class BUtil {

    public static boolean isInteger(BValue bValue) {
        return bValue instanceof BInteger;
    }

    public static boolean isString(BValue bValue) {
        return bValue instanceof BString;
    }

    public static boolean isList(BValue bValue) {
        return bValue instanceof BList;
    }

    public static boolean isDictionary(BValue bValue) {
        return bValue instanceof BDictionary;
    }

    public static BInteger getInteger(BValue bValue) {
        if (!isInteger(bValue)) {
            throw new IllegalStateException("Type is not an int");
        }
        return  ((BInteger) bValue);
    }


    public static BDictionary getDictionary(BValue bValue) {
        if (!isDictionary(bValue)) {
            throw new IllegalStateException("Type is not an Dictionary");
        }
        return ((BDictionary) bValue);
    }

    public static BString getString(BValue bValue) {
        if (!isString(bValue)) {
            throw new IllegalStateException("Type is not an String");
        }
        return ((BString) bValue);

    }

    public static BList getList(BValue bValue) {
        if (!isList(bValue)) {
            throw new IllegalStateException("Type is not an List");
        }
        return ((BList) bValue);

    }
}

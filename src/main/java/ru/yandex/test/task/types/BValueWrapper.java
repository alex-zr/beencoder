package ru.yandex.test.task.types;

/**
 * Created with IntelliJ IDEA.
 * User: al1
 * Date: 25.09.14
 */
public class BValueWrapper {
    private BInteger integer;
    private BString string;
    private BList list;
    private BDictionary dictionary;
    private BType type;

    public BValueWrapper(int intValue) {
        type = BType.INTEGER;
        integer = new BInteger(intValue);
    }

    public boolean isInteger() {
        return type == BType.INTEGER;
    }

    public boolean isString() {
        return type == BType.STRING;
    }

    public boolean isList() {
        return type == BType.LIST;
    }

    public boolean isDictionary() {
        return type == BType.DICTIONARY;
    }

    public BInteger getInteger() {
        if (type != BType.INTEGER) {
            throw new IllegalStateException("Type is not an int");
        }
        return integer;
    }

    public void setInteger(BInteger integer) {
        this.integer = integer;
    }

    public BDictionary getDictionary() {
        if (type != BType.DICTIONARY) {
            throw new IllegalStateException("Type is not an int");
        }
        return dictionary;
    }

    public void setDictionary(BDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public BString getString() {
        if (type != BType.STRING) {
            throw new IllegalStateException("Type is not an int");
        }
        return string;

    }

    public void setString(BString string) {
        this.string = string;
    }

    public BList getList() {
        if (type != BType.LIST) {
            throw new IllegalStateException("Type is not an int");
        }
        return list;

    }

    public void setList(BList list) {
        this.list = list;
    }

    public BType getType() {
        return type;
    }

    public void setType(BType type) {
        this.type = type;
    }
}

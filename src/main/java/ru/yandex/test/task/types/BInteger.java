package ru.yandex.test.task.types;

/**
 * Created with IntelliJ IDEA.
 * User: al1
 * Date: 25.09.14
 */
public class BInteger implements BValue {
    private int value;

    public BInteger() {
    }

    public BInteger(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BInteger{" + value + '}';
    }
}

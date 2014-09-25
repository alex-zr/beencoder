package ru.yandex.test.task.types;

/**
 * Created with IntelliJ IDEA.
 * User: al1
 * Date: 25.09.14
 */
public class BInteger {
    private int value;

    public BInteger() {
    }

    public BInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BInteger{" +
                "value=" + value +
                '}';
    }
}

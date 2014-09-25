package ru.yandex.test.task.types;

/**
 * Created with IntelliJ IDEA.
 * User: al1
 * Date: 25.09.14
 */
public class BString implements BValue {
    private String value;

    public BString(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BString{" +
                "value='" + value + '\'' +
                '}';
    }
}

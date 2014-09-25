package ru.yandex.test.task.types;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: al1
 * Date: 25.09.14
 */
public class BList implements BValue {
    private List<BValue> list = new LinkedList<>();

    public void add(BValue element) {
        list.add(element);
    }

    @Override
    public Object getValue() {
        return list;
    }

    public List<BValue> getList() {
        return this.list;
    }

    @Override
    public String toString() {
        return "BList{" + list + '}';
    }
}

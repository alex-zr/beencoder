package ru.yandex.test.task;

import ru.yandex.test.task.exceptions.SerializationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static ru.yandex.test.task.utils.BUtil.*;

/**
 * Stream serializer for "B-encode" data format
 *
 * @author Oleksandr Roshchupkin
 * @since 24-09-2014
 */

public class BencodeSerializer {
    private final OutputStream outputStream;

    public BencodeSerializer(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Write integer value to b-encode format
     *
     * @param intValue
     */
    public void write(final int intValue) {
        try {
            outputStream.write(INT_PREFIX);
            outputStream.write(String.valueOf(intValue).getBytes());
            outputStream.write(POSTFIX);
        } catch (IOException e) {
            throw new SerializationException("Can't serialize int value", e);
        }
    }

    /**
     * Write String value to b-encode format
     *
     * @param string
     */
    public void write(final String string) {
        try {
            outputStream.write(String.valueOf(string.length()).getBytes());
            outputStream.write(DELIMITER);
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            throw new SerializationException("Can't serialize String value", e);
        }
    }

    /**
     * Write List<Object> to b-encode format recursively, contains following types:
     * [String, Integer, List<Object>, SortedMap<String, Object>]
     *
     * @param list is type of List<Object>
     * @throws IOException
     * @throws SerializationException
     */
    public void write(final List<Object> list) throws IOException {
        outputStream.write(LIST_PREFIX);
        for (Object element : list) {
            writeElement(element);
        }
        outputStream.write(POSTFIX);
    }

    /**
     * Recursion will replaced to iteration in next version
     */
    private void writeElement(final Object element) throws IOException {
        if (isInteger(element)) {
            write(getInteger(element));
        } else if (isString(element)) {
            write(getString(element));
        } else if (isList(element)) {
            write(getList(element));
        } else if (isDictionary(element)) {
            write(getDictionary(element));
        } else {
            throw new SerializationException("Undefined element type '" + (element != null ? element.getClass() : null) +
                    "', use [String, Integer, List<Object>, SortedMap<String, Object>]");
        }
    }

    /**
     * Write SortedSet<String, Object> to b-encode format,
     * key is String
     * value is one of following types:
     * [String, Integer, List<Object>, SortedMap<String, Object>]
     *
     * @param dictionary is type of SortedSet<String, Object>
     * @throws IOException
     */
    public void write(final Map<String, Object> dictionary) throws IOException {
        outputStream.write(DICTIONARY_PREFIX);
        for (Map.Entry<String, Object> pair : dictionary.entrySet()) {
            write(pair.getKey());
            writeElement(pair.getValue());
        }
        outputStream.write(POSTFIX);
    }

    /**
     * Close output stream
     */
    public void close() {
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new SerializationException("Can't close output stream ", e);
        }
    }

    /**
     * Flush output stream
     */
    public void flush() {
        try {
            outputStream.flush();
        } catch (IOException e) {
            throw new SerializationException("Can't flush output stream ", e);
        }
    }
}

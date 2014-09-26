package ru.yandex.test.task;

import ru.yandex.test.task.exceptions.SerializationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static ru.yandex.test.task.utils.BUtil.*;

/**
 * Stream serializer/serializer for "B-encode" data format
 *
 * @author Oleksandr Roshchupkin
 * @since 24-09-2014
 */

public class BencodeSerializer {
    private final OutputStream outputStream;

    public BencodeSerializer(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(int intValue) {
        try {
            outputStream.write(BConstants.INT_PREFIX);
            outputStream.write(String.valueOf(intValue).getBytes());
            outputStream.write(BConstants.POSTFIX);
        } catch (IOException e) {
            throw new SerializationException("Can't serialize int value", e);
        }
    }

    public void write(String string) {
        try {
            outputStream.write(String.valueOf(string.length()).getBytes());
            outputStream.write(BConstants.DELIMITER);
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            throw new SerializationException("Can't serialize String value", e);
        }
    }

    public void write(List<Object> list) throws IOException {
        outputStream.write(BConstants.LIST_PREFIX);
        for (Object element : list) {
            writeElement(element);
        }
        outputStream.write(BConstants.POSTFIX);
    }

    private void writeElement(Object element) throws IOException {
        if (isInteger(element)) {
            write(getInteger(element));
        } else if (isString(element)) {
            write(getString(element));
        } else if (isList(element)) {
            write(getList(element));
        } else if (isDictionary(element)) {
            write(getDictionary(element));
        } else {
            throw new SerializationException("Undefined element " + element);
        }
    }

    public void write(Map<String, Object> dictionary) throws IOException {
        outputStream.write(BConstants.DICTIONARY_PREFIX);
        for (Map.Entry<String, Object> pair : dictionary.entrySet()) {
            write(pair.getKey());
            writeElement(pair.getValue());
        }
        outputStream.write(BConstants.POSTFIX);
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

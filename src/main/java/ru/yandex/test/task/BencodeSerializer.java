package ru.yandex.test.task;

import ru.yandex.test.task.exceptions.SerializationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Stream serializer/serializer for "B-encode" data format
 *
 * @author Oleksandr Roshchupkin
 * @since 24-09-2014
 */

public class BencodeSerializer {
    private InputStream inputStream;
    private OutputStream outputStream;

    public BencodeSerializer(OutputStream outputStream) {
        this.inputStream = inputStream;
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

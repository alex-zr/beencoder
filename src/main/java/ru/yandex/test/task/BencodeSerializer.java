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

    public BencodeSerializer(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public BencodeSerializer(String filePath) {
        // TODO check is present
    }

    public void write(int intValue) {
        try {
            outputStream.write(BConstants.INT_PREFIX);
            outputStream.write(String.valueOf(intValue).getBytes());
            outputStream.write(BConstants.POSTFIX);
        } catch (IOException e) {
            throw new SerializationException();
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

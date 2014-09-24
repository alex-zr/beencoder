package ru.yandex.test.task;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Stream serializer/serializer for "B-encode" data format
 *
 * @author  Oleksandr Roshchupkin
 * @since   24-09-2014
 */

public class BencodeSerializer {
    private InputStream inputStream;
    private OutputStream outputStream;

    public BencodeSerializer(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public BencodeSerializer(String fileName) {
        // TODO check is present
    }

    private static enum Type {
        INTEGER, BYTES, LIST, DICTIONARY
    }
}

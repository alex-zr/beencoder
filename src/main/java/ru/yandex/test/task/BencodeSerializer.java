package ru.yandex.test.task;

import ru.yandex.test.task.exceptions.SerializationException;
import ru.yandex.test.task.types.BDictionary;
import ru.yandex.test.task.types.BList;
import ru.yandex.test.task.types.BValue;
import ru.yandex.test.task.util.BUtil;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Stream serializer/serializer for "B-encode" data format
 *
 * @author Oleksandr Roshchupkin
 * @since 24-09-2014
 */

public class BencodeSerializer {
    private OutputStream outputStream;

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

    public void write(BList bList) throws IOException {
        outputStream.write(BConstants.LIST_PREFIX);
        for (int i = 0; i < bList.getList().size(); i++) {
            BValue element = bList.getList().get(i);
            if (BUtil.isInteger(element)) {
                write(BUtil.getInteger(element).getValue());
            } else if (BUtil.isString(element)) {
                write(BUtil.getString(element).getValue());
            } else if (BUtil.isList(element)) {
                write(BUtil.getList(element));
            } else if (BUtil.isDictionary(element)) {
                write(BUtil.getDictionary(element));
            } else {
                throw new SerializationException("Undefined element " + element);
            }
        }
        outputStream.write(BConstants.POSTFIX);
    }

    private void write(BDictionary dictionary) {
        // TODO implement
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

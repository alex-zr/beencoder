package ru.yandex.test.task;

import ru.yandex.test.task.exceptions.DeserializationException;
import ru.yandex.test.task.types.BInteger;
import ru.yandex.test.task.types.BList;
import ru.yandex.test.task.types.BString;
import ru.yandex.test.task.types.BValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: al1
 * Date: 25.09.14
 */
public class BencodeDeserializer {
    private static final String EXCEPTION_STREAM_END_MSG = "unexpected end of stream while encoding ";
    private InputStream inputStream;
    private LinkedList<Integer> cacheStack;

    public BencodeDeserializer(InputStream inputStream) {
        this.inputStream = inputStream;
        this.cacheStack = new LinkedList<>();
    }

    public BValue readElement() throws IOException {
        Integer aByte;
        while ((aByte = readNextElement()) != null) {
            char symbol = (char) aByte.intValue();
            if (symbol == BConstants.INT_PREFIX) {
                return readInt();
            } else if (Character.isDigit(symbol)) {
                pushBackElement(aByte);
                return readString();
            } else if (symbol == BConstants.LIST_PREFIX) {
                return readList();
            } else {
                throw new DeserializationException("Unexpected symbol - " + symbol);
            }
        }
        throw new IOException("Unexpected end of stream or stream is empty");
    }

    private BValue readInt() throws IOException {
        Integer aByte;
        StringBuilder intBuilder = new StringBuilder();
        while ((aByte = readNextElement()) != null && !isPostfix(aByte)) {
            if (aByte < 0) {
                throw new IOException(EXCEPTION_STREAM_END_MSG + "Integer");
            }
            char symbol = (char) aByte.intValue();
            intBuilder.append(symbol);
        }
        // check if postfix present
        if (aByte == null || !isPostfix(aByte)) {
            throw new DeserializationException("Unexpected end of int, '" + BConstants.POSTFIX + "' expected");
        }
        String strInt = intBuilder.toString();
        try {
            return new BInteger(Integer.parseInt(strInt));
        } catch (NumberFormatException nfe) {
            throw new DeserializationException(EXCEPTION_STREAM_END_MSG + "Integer [" + strInt + "]");
        }
    }


    private BValue readString() throws IOException {
        int length = readStringLength();
        String string = readStringContent(length);
        return new BString(string);
    }

    private int readStringLength() throws IOException {
        int aByte;
        StringBuilder lengthBuilder = new StringBuilder();
        while ((aByte = readNextElement()) >= 0 && !isDelimiter(aByte)) {
            if (aByte < 0) {
                throw new IOException(EXCEPTION_STREAM_END_MSG + "String");
            }
            char symbol = (char) aByte;
            lengthBuilder.append(symbol);
        }

        String strLength = lengthBuilder.toString();
        try {
            return Integer.parseInt(strLength);
        } catch (NumberFormatException nfe) {
            throw new DeserializationException("String length is incorrect [" + strLength + "]");
        }
    }

    private String readStringContent(int length) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(length);
        int aByte;
        for (int i = 0; i < length; i++) {
            aByte = inputStream.read();
            if (aByte < 0) {
                throw new DeserializationException("Length of string is less then expected - " + i + "," + length);
            }
            stringBuilder.append((char) aByte);
        }
        return stringBuilder.toString();
    }

    private BList readList() throws IOException {
        Integer aByte;
        BList list = new BList();
        while ((aByte = readNextElement()) != null && !isPostfix(aByte)) {
            pushBackElement(aByte);
            list.add(readElement());
        }
        // check is list postfix present
        if (aByte == null || !isPostfix(aByte)) {
            throw new DeserializationException("Unexpected end of list, '" + BConstants.POSTFIX + "' expected");
        }
        return list;
    }

    private boolean isDelimiter(int aByte) {
        return (((char) aByte) == BConstants.DELIMITER);
    }

    private boolean isPostfix(int aByte) {
        return (((char) aByte) == BConstants.POSTFIX);
    }

    private void pushBackElement(Integer aByte) {
        cacheStack.push(aByte);
    }

    private Integer readNextElement() throws IOException {
        Integer aByte;
        if (cacheStack.peek() != null) {
            return cacheStack.pop();
        } else if ((aByte = inputStream.read()) >= 0) {
            return aByte;
        } else {
            return null;
        }
    }
}

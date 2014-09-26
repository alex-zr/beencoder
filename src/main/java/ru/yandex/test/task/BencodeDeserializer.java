package ru.yandex.test.task;

import ru.yandex.test.task.exceptions.DeserializationException;
import ru.yandex.test.task.utils.BUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Stream deserializer for "B-encode" data format
 *
 * @author Oleksandr Roshchupkin
 * @since 25-09-2014
 */
public class BencodeDeserializer {
    private static final String EXCEPTION_STREAM_END_MSG = "unexpected end of stream while encoding ";
    private final InputStream inputStream;
    private final LinkedList<Integer> cacheStack; // May be better to extract stack to separated class
    private final StringBuilder errorContext;

    public BencodeDeserializer(final InputStream inputStream) {
        this.inputStream = inputStream;
        this.cacheStack = new LinkedList<>();
        this.errorContext = new StringBuilder();
    }

    /**
     * Deserialized recursively one element of stream into the following types:
     * [String, Integer, List<Object>, SortedMap<String, Object>]
     *
     * @return
     * @throws IOException
     * @throws DeserializationException
     */
    public Object readElement() throws IOException {
        Integer aByte;
        while ((aByte = readNextElement()) != null) {
            char symbol = (char) aByte.intValue();
            if (symbol == BUtil.INT_PREFIX) {
                return readInt();
            } else if (Character.isDigit(symbol)) {
                pushBackElement(aByte);
                return readString();
            } else if (symbol == BUtil.LIST_PREFIX) {
                return readList();
            } else if (symbol == BUtil.DICTIONARY_PREFIX) {
                return readDictionary();
            } else {
                throw new DeserializationException("Unexpected symbol '" + symbol + "'");
            }
        }
        throw new IOException("Unexpected end of stream or stream is empty " + errorContext);
    }

    private Integer readInt() throws IOException {
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
            throw new DeserializationException("Unexpected end of int, '" + BUtil.POSTFIX + "' expected");
        }
        String strInt = intBuilder.toString();
        try {
            return Integer.parseInt(strInt);
        } catch (NumberFormatException nfe) {
            throw new DeserializationException(EXCEPTION_STREAM_END_MSG + "Integer [" + strInt + "]");
        }
    }

    private String readString() throws IOException {
        int length = readStringLength();
        checkDelimiter();
        return readStringContent(length);
    }

    private int readStringLength() throws IOException {
        int aByte;
        StringBuilder lengthBuilder = new StringBuilder();
        while ((aByte = readNextElement()) >= 0 && !isDelimiter(aByte) && Character.isDigit(aByte)) {
            if (aByte < 0) {
                throw new IOException(EXCEPTION_STREAM_END_MSG + "String");
            }
            char symbol = (char) aByte;
            lengthBuilder.append(symbol);
        }

        if (isDelimiter(aByte)) {
            pushBackElement(aByte);
        }
        String strLength = lengthBuilder.toString();
        try {
            return Integer.parseInt(strLength);
        } catch (NumberFormatException nfe) {
            throw new DeserializationException("String length is incorrect [" + strLength + "]");
        }
    }

    private String readStringContent(final int length) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(length);
        int aByte;
        for (int i = 0; i < length; i++) {
            aByte = readNextElement();
            if (aByte < 0) {
                throw new DeserializationException("Length of string is less then expected - " + i + "," + length);
            }
            stringBuilder.append((char) aByte);
        }
        return stringBuilder.toString();
    }

    private List readList() throws IOException {
        Integer aByte;
        List<Object> list = new LinkedList<>();
        while ((aByte = readNextElement()) != null && !isPostfix(aByte)) {
            pushBackElement(aByte);
            list.add(readElement());
        }
        checkPostfix(aByte);
        return list;
    }

    private void checkPostfix(final Integer aByte) {
        if (aByte == null || !isPostfix(aByte)) {
            throw new DeserializationException("Unexpected end of element, "+ errorContext + "'" + BUtil.POSTFIX + "' expected");
        }
    }

    private Map readDictionary() throws IOException {
        Integer aByte;
        Map<String, Object> dictionary = new TreeMap<>();
        while ((aByte = readNextElement()) != null && !isPostfix(aByte)) {
            pushBackElement(aByte);
            String key = readString();

            Object value = readElement();
            dictionary.put(key, value);
        }
        checkPostfix(aByte);
        return dictionary;
    }

    private void checkDelimiter() throws IOException {
        Integer aByte = readNextElement();
        if (aByte == null) {
            throw new DeserializationException("Unexpected end of expression after " + errorContext);
        } else if (!isDelimiter(aByte)) {
            throw new DeserializationException("Unexpected symbol, '" + (char)aByte.byteValue());
        }
    }

    private boolean isDelimiter(final int aByte) {
        return (((char) aByte) == BUtil.DELIMITER);
    }

    private boolean isPostfix(final int aByte) {
        return (((char) aByte) == BUtil.POSTFIX);
    }

    private void pushBackElement(final Integer aByte) {
        cacheStack.push(aByte);
    }

    private Integer readNextElement() throws IOException {
        Integer aByte;
        Integer resultByte;
        if (cacheStack.peek() != null) {
            resultByte = cacheStack.pop();
        } else if ((aByte = inputStream.read()) >= 0) {
            resultByte = aByte;
            prepareContext(resultByte);
        } else {
            throw new DeserializationException("Unexpected end of element after '" + errorContext + "'");
        }

        return resultByte;
    }

    private void prepareContext(final Integer resultByte) {
        if (errorContext.length() > 10) {
            errorContext.delete(0, errorContext.length() - 1);
        }
        errorContext.append((char)resultByte.byteValue());
    }
}

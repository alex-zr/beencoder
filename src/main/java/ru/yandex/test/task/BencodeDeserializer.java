package ru.yandex.test.task;

import ru.yandex.test.task.exceptions.DeserializationException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static ru.yandex.test.task.utils.BUtil.*;

/**
 * Stream deserializer for "B-encode" data format
 *
 * @author Oleksandr Roshchupkin
 * @since 25-09-2014
 */
public class BencodeDeserializer implements Closeable {
    private static final String EXCEPTION_STREAM_END_MSG = "unexpected end of stream while encoding ";
    private final InputStream inputStream;
    private final LinkedList<Integer> byteCacheStack; // May be better to extract stack to separated class
    private final LinkedList<Object> elementCacheStack;
    private final StringBuilder errorContext;

    public BencodeDeserializer(final InputStream inputStream) {
        this.inputStream = inputStream;
        this.byteCacheStack = new LinkedList<>();
        this.elementCacheStack = new LinkedList<>();
        this.errorContext = new StringBuilder();
    }

    /**
     * Check if stream contains next int value
     *
     * @return true if value is iny
     */
    public boolean hasNext() {
        Object element;
        try {
            element = readElement();
        } catch (DeserializationException e) {
            return false;
        }
        pushBackElement(element);
        return isInteger(element) || isString(element) || isList(element) || isDictionary(element);
    }

    /**
     * Check if stream contains next Integer value
     *
     * @return true if value is Integer
     */
    public boolean hasNextInt() {
        Object element;
        try {
            element = readElement();
        } catch (DeserializationException e) {
            return false;
        }
        pushBackElement(element);
        return isInteger(element);
    }

    /**
     * Check if stream contains next Integer value
     *
     * @return true if value is String
     */
    public boolean hasNextString() {
        Object element;
        try {
            element = readElement();
        } catch (DeserializationException e) {
            return false;
        }
        pushBackElement(element);
        return isString(element);
    }

    /**
     * Check if stream contains next List value
     *
     * @return true if value is List
     */
    public boolean hasNextList() {
        Object element;
        try {
            element = readElement();
        } catch (DeserializationException e) {
            return false;
        }
        pushBackElement(element);
        return isList(element);
    }

    /**
     * Check if stream contains next Dictionary value
     *
     * @return true if value is Dictionary
     */
    public boolean hasNextDictionary() {
        Object element;
        try {
            element = readElement();
        } catch (DeserializationException e) {
            return false;
        }
        pushBackElement(element);
        return isDictionary(element);
    }

    /**
     * Get next int value from stream
     *
     * @return int value from stream
     * @throws DeserializationException
     */
    public int nextInt() {
        Object element = readElement();
        if (!isInteger(element)) {
            pushBackElement(element);
            throw new DeserializationException("Read object is not an int type");
        }
        return (Integer)element;
    }

    /**
     * Get next String value from stream
     *
     * @return String value from stream
     * @throws DeserializationException
     */
    public String nextString() {
        Object element = readElement();
        if (!isString(element)) {
            pushBackElement(element);
            throw new DeserializationException("Read object is not a String type");
        }
        return (String)element;
    }

    /**
     * Get next List value from stream
     *
     * @return List value from stream
     * @throws DeserializationException
     */
    @SuppressWarnings("unchecked")
    public List nextList() {
        Object element = readElement();
        if (!isList(element)) {
            pushBackElement(element);
            throw new DeserializationException("Read object is not a List type");
        }
        return (List)element;
    }

    /**
     * Get next Dictionary(Map<String, Object>) value from stream
     *
     * @return List value from stream
     * @throws DeserializationException
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> nextDictionary() {
        Object element = readElement();
        if (!isDictionary(element)) {
            pushBackElement(element);
            throw new DeserializationException("Read object is not a Dictionary type");
        }
        return (Map<String, Object>)element;
    }

    /**
     * Close output stream
     */
    public void close() {
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new DeserializationException("Can't close input stream ", e);
        }
    }

    /**
     * Deserialized recursively one element of stream into the following types:
     * [String, int, List<Object>, SortedMap<String, Object>]
     *
     * @return read element
     * @throws DeserializationException
     */
    private Object readElement() {
        Integer aByte;
        if (elementCacheStack.peek() != null) {
            return elementCacheStack.pop();
        }
        try {
            if ((aByte = nextByte()) != null) {
                char symbol = (char) aByte.intValue();
                if (symbol == INT_PREFIX) {
                    return readInt();
                } else if (Character.isDigit(symbol)) {
                    pushBackByte(aByte);
                    return readString();
                } else if (symbol == LIST_PREFIX) {
                    return readList();
                } else if (symbol == DICTIONARY_PREFIX) {
                    return readDictionary();
                } else {
                    throw new DeserializationException("Unexpected symbol '" + symbol + "'");
                }
            }
        } catch (IOException e) {
            throw new DeserializationException("Error while reading from stream", e);
        }
        throw new DeserializationException("Unexpected end of stream or stream is empty " + errorContext);
    }

    private Integer readInt() throws IOException {
        Integer aByte;
        StringBuilder intBuilder = new StringBuilder();
        while ((aByte = nextByte()) != null && !isPostfix(aByte)) {
            if (aByte < 0) {
                throw new IOException(EXCEPTION_STREAM_END_MSG + "Integer");
            }
            char symbol = (char) aByte.intValue();
            intBuilder.append(symbol);
        }
        // check if postfix present
        if (aByte == null || !isPostfix(aByte)) {
            throw new DeserializationException("Unexpected end of int, '" + POSTFIX + "' expected");
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
        while ((aByte = nextByte()) >= 0 && !isDelimiter(aByte) && Character.isDigit(aByte)) {
            if (aByte < 0) {
                throw new IOException(EXCEPTION_STREAM_END_MSG + "String");
            }
            char symbol = (char) aByte;
            lengthBuilder.append(symbol);
        }

        if (isDelimiter(aByte)) {
            pushBackByte(aByte);
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
            aByte = nextByte();
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
        while ((aByte = nextByte()) != null && !isPostfix(aByte)) {
            pushBackByte(aByte);
            list.add(readElement());
        }
        checkPostfix(aByte);
        return list;
    }

    private void checkPostfix(final Integer aByte) {
        if (aByte == null || !isPostfix(aByte)) {
            throw new DeserializationException("Unexpected end of element, "+ errorContext + "'" + POSTFIX + "' expected");
        }
    }

    private void pushBackElement(Object element) {
        elementCacheStack.push(element);
    }

    private Map readDictionary() throws IOException {
        Integer aByte;
        Map<String, Object> dictionary = new TreeMap<>();
        while ((aByte = nextByte()) != null && !isPostfix(aByte)) {
            pushBackByte(aByte);
            String key = readString();

            Object value = readElement();
            dictionary.put(key, value);
        }
        checkPostfix(aByte);
        return dictionary;
    }

    private void checkDelimiter() throws IOException {
        Integer aByte = nextByte();
        if (aByte == null) {
            throw new DeserializationException("Unexpected end of expression after " + errorContext);
        } else if (!isDelimiter(aByte)) {
            throw new DeserializationException("Unexpected symbol, '" + (char)aByte.byteValue());
        }
    }

    private boolean isDelimiter(final int aByte) {
        return (((char) aByte) == DELIMITER);
    }

    private boolean isPostfix(final int aByte) {
        return (((char) aByte) == POSTFIX);
    }

    private void pushBackByte(final Integer aByte) {
        byteCacheStack.push(aByte);
    }

    private Integer nextByte() throws IOException {
        Integer aByte;
        Integer resultByte;
        if (byteCacheStack.peek() != null) {
            resultByte = byteCacheStack.pop();
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

package ru.yandex.test.task;

import ru.yandex.test.task.exceptions.DeserializationException;
import ru.yandex.test.task.types.BValueWrapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: al1
 * Date: 25.09.14
 */
public class BencodeDeserializer {
    private static final String EXCEPTION_STREAM_END_MSG = "unexpected end of stream while encoding ";
    private InputStream inputStream;

    public BencodeDeserializer(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public BValueWrapper readElement() throws IOException {
        BValueWrapper wrapper;
        int aByte;
        while ((aByte = inputStream.read()) >= 0) {
            char symbol = (char) aByte;
            if (symbol == BConstants.INT_PREFIX) {
                return readInt();
            } else if (Character.isDigit(symbol)) {
                return readString(symbol);
            } else {
                throw new DeserializationException("Unexpected symbol - " + symbol);
            }
        }
        throw new IOException("Unexpected end of stream or stream is empty");
    }

    private BValueWrapper readInt() throws IOException {
        int aByte;
        StringBuilder intBuilder = new StringBuilder();
        while ((aByte = inputStream.read()) >= 0 && !isPostfix(aByte)) {
            if (aByte < 0) {
                throw new IOException(EXCEPTION_STREAM_END_MSG + "Integer");
            }
            char symbol = (char) aByte;
            intBuilder.append(symbol);
        }

        String strInt = intBuilder.toString();
        try {
            return new BValueWrapper(Integer.parseInt(strInt));
        } catch (NumberFormatException nfe) {
            throw new DeserializationException(EXCEPTION_STREAM_END_MSG + "Integer [" + strInt + "]");
        }
    }


    private BValueWrapper readString(char symbol) throws IOException {
        int length = readStringLength(symbol);
        String string = readStringContent(length);
        return new BValueWrapper(string);
    }

    private int readStringLength(char firstSymbol) throws IOException {
        int aByte;
        StringBuilder lengthBuilder = new StringBuilder();
        if (!isDelimiter(firstSymbol)) {
            lengthBuilder.append(firstSymbol);
        }
        while ((aByte = inputStream.read()) >= 0 && !isDelimiter(aByte)) {
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

/*        char[] value = new char[length];
        int b;
        for (int i = 0; i < length; i++) {
            b = bis.read();
            if (b == -1) {
                throw new IOException(BC_MSG_EXCEPTION_STREAM_END
                        + "String");
            } else {
                value[i] = (char) b;
            }
        }
        String s = new String(value);*/
    }

    private boolean isDelimiter(int aByte) {
        return (((char) aByte) == BConstants.DELIMITER);
    }

    private boolean isPostfix(int aByte) {
        return (((char) aByte) == BConstants.POSTFIX);
    }
}

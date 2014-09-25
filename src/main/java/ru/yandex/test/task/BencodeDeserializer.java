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
            }
        }
        throw new IOException("Unexpected end of stream");
    }

    private BValueWrapper readInt() throws IOException {
        int aByte = 0;
        StringBuilder intBuilder = new StringBuilder();
        while ((aByte = inputStream.read()) >= 0 && (((char) aByte) != BConstants.POSTFIX)) {
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


}

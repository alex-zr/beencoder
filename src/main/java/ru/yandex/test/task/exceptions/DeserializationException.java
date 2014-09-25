package ru.yandex.test.task.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: al1
 * Date: 25.09.14
 */
public class DeserializationException extends RuntimeException {
    public DeserializationException() {
    }

    public DeserializationException(String message) {
        super(message);
    }

    public DeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}

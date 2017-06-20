package ru.sberbank.mobile.core.parser;

public class SerializeException extends Exception {
    public SerializeException() {
    }

    public SerializeException(String detailMessage) {
        super(detailMessage);
    }

    public SerializeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SerializeException(Throwable throwable) {
        super(throwable);
    }
}

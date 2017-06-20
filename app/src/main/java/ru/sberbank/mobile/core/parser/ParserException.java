package ru.sberbank.mobile.core.parser;

public class ParserException extends Exception {

    public ParserException() {
    }

    public ParserException(String detailMessage) {
        super(detailMessage);
    }

    public ParserException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ParserException(Throwable throwable) {
        super(throwable);
    }
}

package ru.sberbank.mobile.core.network;

import java.io.IOException;
import java.io.OutputStream;

import ru.sberbank.mobile.core.bean.io.Encoding;
import ru.sberbank.mobile.core.bean.io.MimeType;

/**
 * Created by Trikhin P O on 07.06.2016.
 */
public interface IBodySender {

    MimeType getMimeType();

    void prepareData(Encoding encoding);

    String getBoundary();

    long length();

    void flushBody(OutputStream sink) throws IOException;
}

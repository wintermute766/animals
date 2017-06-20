package ru.sberbank.mobile.core.parser;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Trikhin P O on 07.06.2016.
 */
public interface IParser {

    <T> T parse(@NonNull InputStream source, @NonNull Class<T> clazz) throws IOException, ParserException;

    void serialize(@NonNull OutputStream sink, @NonNull Object object) throws IOException, SerializeException;

}

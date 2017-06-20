package ru.sberbank.mobile.core.parser;

import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.sberbank.mobile.core.bean.io.Encoding;

/**
 * Created by Trikhin P O on 07.06.2016.
 */
public class ExtendedParser implements IParser {

    private final IParser mInnerParser;

    public ExtendedParser(IParser innerParser) {
        this.mInnerParser = innerParser;
    }

    @Override
    public <T> T parse(@NonNull InputStream source, @NonNull Class<T> clazz) throws IOException, ParserException {
        return mInnerParser.parse(source, clazz);
    }

    public <T> T parse(@NonNull String data, @NonNull Class<T> clazz) throws IOException, ParserException {
        InputStream source = new ByteArrayInputStream(data.getBytes());
        T result = null;
        try {
            result = mInnerParser.parse(source, clazz);
        } finally {
            try {
                source.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public <T> T parse(@NonNull File file, @NonNull Class<T> clazz) throws IOException, ParserException {
        T result = null;
        InputStream source = null;
        try {
            source = new FileInputStream(file);
            source = new BufferedInputStream(source);
            result = mInnerParser.parse(source, clazz);
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public void serialize(@NonNull OutputStream sink, @NonNull Object object) throws IOException, SerializeException {
        mInnerParser.serialize(sink, object);
    }

    public void serialize(StringBuilder data, Encoding encoding, Object object) throws IOException, SerializeException {
        ByteArrayOutputStream sink = new ByteArrayOutputStream();
        try {
            mInnerParser.serialize(sink, object);
            sink.flush();
            data.append(sink.toString(encoding.getName()));
        } finally {
            try {
                sink.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void serialize(@NonNull File file, @NonNull Object object) throws IOException, SerializeException {
        OutputStream sink = null;
        try {
            sink = new FileOutputStream(file);
            sink = new BufferedOutputStream(sink);
            mInnerParser.serialize(sink, object);
        } finally {
            if (sink != null) {
                try {
                    sink.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

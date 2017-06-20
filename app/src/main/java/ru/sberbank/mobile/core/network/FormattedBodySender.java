package ru.sberbank.mobile.core.network;

import android.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.sberbank.mobile.core.bean.io.Encoding;
import ru.sberbank.mobile.core.bean.io.MimeType;
import ru.sberbank.mobile.core.parser.ExtendedParser;
import ru.sberbank.mobile.core.parser.SerializeException;

/**
 * Created by Trikhin P O on 08.06.2016.
 */
public class FormattedBodySender implements IBodySender {

    private static final String BOUNDARY_BASE = "Asrf456BGe4h";
    private static final String LINE_NEW = "\r\n";
    private static final String LINE_DASH = "--";
    private static final String LINE_CONTENT = "Content-Disposition: form-data; name=\"%s\"";

    private final ExtendedParser mParser;
    private final Object mEntity;
    private final MimeType mMimeType;
    private final Map<String, String> mNameValuePairs;
    private final String mBoundary;
    private boolean mPrepared;

    private ByteArrayOutputStream mDataBuffer;

    public FormattedBodySender(ExtendedParser parser, Object entity, MimeType mimeType) {
        this.mParser = parser;
        this.mEntity = entity;
        this.mMimeType = mimeType;
        this.mNameValuePairs = new LinkedHashMap<>();
        this.mBoundary = BOUNDARY_BASE + System.currentTimeMillis();
        this.mPrepared = false;
    }

    public FormattedBodySender add(String name, String value) {
        checkModificationPossibility();
        mNameValuePairs.put(name, value);
        return this;
    }

    public FormattedBodySender add(String name, boolean value) {
        return add(name, String.valueOf(value));
    }

    public FormattedBodySender add(String name, int value) {
        return add(name, String.valueOf(value));
    }

    public FormattedBodySender add(String name, double value) {
        return add(name, String.valueOf(value));
    }

    public FormattedBodySender add(String name, long value) {
        return add(name, String.valueOf(value));
    }

    public FormattedBodySender add(String name, Object value, Encoding encoding) {
        StringBuilder jsonBuilder = new StringBuilder();
        try {
            mParser.serialize(jsonBuilder, encoding, value);
        } catch (IOException e) {
        } catch (SerializeException e) {
        }
        return add(name, jsonBuilder.toString());
    }

    public FormattedBodySender addAll(List<Pair<String, String>> nameValuePairs) {
        checkModificationPossibility();
        for (Pair<String, String> pair : nameValuePairs) {
            mNameValuePairs.put(pair.first, pair.second);
        }
        return this;
    }

    @Override
    public MimeType getMimeType() {
        return mMimeType;
    }

    @Override
    public void prepareData(Encoding encoding) {
        mPrepared = true;
        mDataBuffer = new ByteArrayOutputStream();
        try {
            if (mMimeType.equals(MimeType.MULTI_PART_FORM_DATA)) {
                buildMultipartForm(encoding);
            } else {
                mParser.serialize(mDataBuffer, mEntity);

            }
        } catch (IOException e) {
        } catch (SerializeException e) {
        }
    }

    @Override
    public String getBoundary() {
        if (mMimeType.equals(MimeType.MULTI_PART_FORM_DATA)) {
            return mBoundary;
        }
        throw new UnsupportedOperationException("No need to implement");
    }

    @Override
    public long length() {
        if (mMimeType.equals(MimeType.MULTI_PART_FORM_DATA)) {
            return mDataBuffer.size();
        }
        throw new UnsupportedOperationException("No need to implement");
    }

    @Override
    public void flushBody(OutputStream sink) throws IOException {
        String body = new String(mDataBuffer.toByteArray());
        mDataBuffer.writeTo(sink);
        sink.flush();
        mDataBuffer.close();
        mDataBuffer = null;
    }

    private void checkModificationPossibility() {
        if (mPrepared) {
            throw new IllegalStateException("Нельзя добавлять значения после того, как подготовили BodySender");
        }
        if (!mMimeType.equals(MimeType.MULTI_PART_FORM_DATA)) {
            throw new IllegalStateException("Метод доступен только для mimeType=MimeType.MULTI_PART_FORM_DATA");
        }
    }

    private void buildMultipartForm(Encoding encoding) throws IOException {
        appendKeyValuesToMultipartForm(encoding);
        addFooter();
    }

    private void appendKeyValuesToMultipartForm(Encoding encoding) throws IOException {
        String strEncoding = encoding.getName();
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : mNameValuePairs.entrySet()) {
            builder.append(LINE_DASH).append(mBoundary).append(LINE_NEW)
                    .append(String.format(LINE_CONTENT, entry.getKey())).append(LINE_NEW)
                    .append(LINE_NEW).append(entry.getValue())
                    .append(LINE_NEW);
            mDataBuffer.write(builder.toString().getBytes());
            builder.delete(0, builder.length());
        }
    }

    private void addHeader() throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(LINE_NEW);
        mDataBuffer.write(builder.toString().getBytes());
    }

    private void addFooter() throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(LINE_NEW);
        builder.append(LINE_DASH)
                .append(mBoundary)
                .append(LINE_DASH);
        builder.append(LINE_NEW);
        mDataBuffer.write(builder.toString().getBytes());
    }
}

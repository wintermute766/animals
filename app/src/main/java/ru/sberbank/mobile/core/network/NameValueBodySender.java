package ru.sberbank.mobile.core.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.sberbank.mobile.core.bean.io.Encoding;
import ru.sberbank.mobile.core.bean.io.FileInfo;
import ru.sberbank.mobile.core.bean.io.MimeType;
import ru.sberbank.mobile.core.utils.CollectionUtils;
import ru.sberbank.mobile.core.utils.FileUtils;

/**
 * @author Trikhin P O
 * @date 07.06.2016.
 */
public class NameValueBodySender implements IBodySender {

    private static final String LOG_TAG = NameValueBodySender.class.getSimpleName();
    private static final String BOUNDARY_BASE = "Asrf456BGe4h";
    private static final String LINE_NEW = "\r\n";
    private static final String LINE_DASH = "--";
    private static final String LINE_CONTENT = "Content-Disposition: form-data; name=\"%s\"";
    private static final String LINE_FILENAME = "; filename=\"%s\"";
    private static final String LINE_CONTENT_TYPE = "Content-Type: %s";

    private final Map<String, String> mNameValuePairs = new LinkedHashMap<>();
    private FileInfo mFileInfo;
    private String mBoundary;

    private boolean mPrepared;
    private ByteArrayOutputStream mDataBuffer;
    private ByteArrayOutputStream mBufferBeforeFile;
    private ByteArrayOutputStream mBufferAfterFile;

    public NameValueBodySender add(@NonNull String name, @Nullable String value) {
        if (value != null) {
            mNameValuePairs.put(name, value);
        }
        return this;
    }

    public NameValueBodySender add(String name, boolean value) {
        checkModificationPossibility();
        return add(name, String.valueOf(value));
    }

    public NameValueBodySender add(String name, int value) {
        return add(name, String.valueOf(value));
    }

    public NameValueBodySender add(String name, double value) {
        return add(name, String.valueOf(value));
    }

    public NameValueBodySender add(String name, long value) {
        return add(name, String.valueOf(value));
    }

    public NameValueBodySender addAll(List<Pair<String, String>> nameValuePairs) {
        for (Pair<String, String> pair : nameValuePairs) {
            mNameValuePairs.put(pair.first, pair.second);
        }
        return this;
    }

    public NameValueBodySender add(String name, List<Object> listOfObjects) {
        String concatObjects = "";
        if (listOfObjects != null && listOfObjects.size() > 0) {
            for (Object objectElement : listOfObjects) {
                concatObjects = concatObjects + objectElement.toString() + ",";
            }
            concatObjects = concatObjects.substring(0, concatObjects.length() - 1);
        }
        return add(name, concatObjects);
    }

    public void setFileInfo(FileInfo fileInfo) {
        checkModificationPossibility();
        this.mFileInfo = fileInfo;
        mBoundary = BOUNDARY_BASE + System.currentTimeMillis();
    }

    public Map<String, String> getNameValuePairs() {
        return CollectionUtils.wrapMap(mNameValuePairs);
    }

    private void checkModificationPossibility() {
        if (mPrepared) {
            throw new IllegalStateException("Нельзя добавлять значения после того, как подготовили BodySender");
        }
    }

    @Override
    public MimeType getMimeType() {
        MimeType type = isMultiPartData() ? MimeType.MULTI_PART_FORM_DATA : MimeType.APPLICATION_X_WWW_FORM_URLENCODED;
        return type;
    }

    @Override
    public void prepareData(Encoding encoding) {
        mPrepared = true;
        try {
            if (isMultiPartData()) {
                buildMultipartForm(encoding);
            } else {
                mDataBuffer = new ByteArrayOutputStream();
                String form = PostDataBuilder.buildUrlEncodedData(mNameValuePairs, encoding);
                mDataBuffer.write(form.getBytes());
            }
        } catch (IOException ex) {
        }
    }

    @Override
    public String getBoundary() {
        return mBoundary;
    }

    @Override
    public long length() {
        final long size = dataSize(mDataBuffer) + dataSize(mBufferBeforeFile) + dataSize(mFileInfo) + dataSize(mBufferAfterFile);
        return size;
    }


    @Override
    public void flushBody(OutputStream sink) throws IOException {
        String body = mDataBuffer != null && mDataBuffer.size() > 0 ? new String(mDataBuffer.toByteArray()) : "Empty";
        if (isMultiPartData()) {
            multipartDataFlush(sink);
        } else {
            defaultFlush(sink);
        }
    }

    private void defaultFlush(OutputStream sink) throws IOException {
        mDataBuffer.writeTo(sink);
        sink.flush();
        closeByteArrayOutputStream(mDataBuffer);
        mDataBuffer = null;
    }

    private void multipartDataFlush(OutputStream sink) throws IOException {
        writeToOutput(mDataBuffer, sink);
        writeToOutput(mBufferBeforeFile, sink);
        FileUtils.copyFromFile(mFileInfo.getFile(), sink);
        writeToOutput(mBufferAfterFile, sink);
        sink.flush();
        closeByteArrayOutputStream(mDataBuffer);
        mDataBuffer = null;
        closeByteArrayOutputStream(mBufferBeforeFile);
        mBufferBeforeFile = null;
        closeByteArrayOutputStream(mBufferAfterFile);
        mBufferAfterFile = null;
    }

    private void buildMultipartForm(Encoding encoding) throws IOException {
        mDataBuffer = generateKeyValuesToMultipartForm(encoding, false);
        mBufferBeforeFile = generateFileHeader(mDataBuffer != null && mDataBuffer.size() > 0);
        mBufferAfterFile = generateFileFooter();
    }

    private ByteArrayOutputStream generateKeyValuesToMultipartForm(Encoding encoding, boolean newLine) throws IOException {
        ByteArrayOutputStream result = null;
        String strEncoding = encoding.getName();
        for (Map.Entry<String, String> entry : mNameValuePairs.entrySet()) {
            StringBuilder builder = new StringBuilder();
            result = new ByteArrayOutputStream();
            if (newLine) {
                builder.append(LINE_NEW);
            }
            builder.append(LINE_DASH).append(mBoundary).append(LINE_NEW)
                    .append(String.format(LINE_CONTENT, entry.getKey())).append(LINE_NEW)
                    .append(LINE_NEW).append(URLEncoder.encode(entry.getValue(), strEncoding));
            result.write(builder.toString().getBytes());
            builder.delete(0, builder.length());
        }
        return result;
    }

    private ByteArrayOutputStream generateFileHeader(boolean newLine) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (newLine) {
            builder.append(LINE_NEW);
        }
        builder.append(LINE_DASH).append(mBoundary).append(LINE_NEW)
                .append(String.format(LINE_CONTENT, mFileInfo.getKey()));
        if (!TextUtils.isEmpty(mFileInfo.getName())) {
            builder.append(String.format(LINE_FILENAME, mFileInfo.getName()));
        }
        builder.append(LINE_NEW);
        builder.append(String.format(LINE_CONTENT_TYPE, mFileInfo.getMimeType().toString()))
                .append(LINE_NEW);
        builder.append(LINE_NEW);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        result.write(builder.toString().getBytes());
        return result;
    }

    private ByteArrayOutputStream generateFileFooter() throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(LINE_NEW).append(LINE_DASH).append(mBoundary).append(LINE_DASH).append(LINE_NEW);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        result.write(builder.toString().getBytes());
        return result;
    }

    private boolean isMultiPartData() {
        return mFileInfo != null;
    }

    private long dataSize(@Nullable ByteArrayOutputStream data) {
        return data != null ? data.size() : 0;
    }

    private long dataSize(@Nullable FileInfo fileInfo) {
        return fileInfo != null ? fileInfo.getFile().length() : 0;
    }

    private void writeToOutput(@Nullable ByteArrayOutputStream fromStream,
                               @NonNull OutputStream toStream) throws IOException {
        if (fromStream != null && fromStream.size() > 0) {
            toStream.write(fromStream.toByteArray());
        }
    }

    private void closeByteArrayOutputStream(@Nullable ByteArrayOutputStream stream) throws IOException {
        if (stream != null) {
            stream.close();
        }
    }
}

package ru.sberbank.mobile.core.network;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.sberbank.mobile.core.bean.io.Encoding;
import ru.sberbank.mobile.core.parser.ExtendedParser;
import ru.sberbank.mobile.core.parser.ParserException;
import ru.sberbank.mobile.core.utils.CollectionUtils;

/**
 * @inheritDoc
 */
public class DefaultHttpResponseReceiver<T> implements IHttpResponseReceiver<T> {

    private final static String LOG_TAG = "DefaultHttpResponseReceiver";

    private final Class<T> mClazz;
    private final Encoding mEncoding;
    private final ExtendedParser mParser;
    private final List<IHeaderHandler> mHeaderHandlers;
    private T mResult;
    private int mHttpResponseCode;

    public DefaultHttpResponseReceiver(Class<T> clazz, Encoding encoding, ExtendedParser parser) {
        this(clazz, encoding, parser, new ArrayList<IHeaderHandler>());
    }

    public DefaultHttpResponseReceiver(Class<T> clazz, Encoding encoding, ExtendedParser parser, List<IHeaderHandler> headerHandlers) {
        this.mClazz = clazz;
        this.mEncoding = encoding;
        this.mParser = parser;
        this.mHeaderHandlers = new ArrayList<>(headerHandlers);
    }

    public void handleResponse(@NonNull InputStream inputStream, @NonNull Map<String, List<String>> headers)
            throws IOException, ParserException {
        for (IHeaderHandler handler : mHeaderHandlers) {
            handler.handleHeaders(headers);
        }

        // Разбор ответа сервера
        mResult = mParser.parse(inputStream, mClazz);
    }

    //для StubHttpConnector`а
    public void setResult(final T result){
        mResult = result;
    }

    public final T getResult() {
        return mResult;
    }

    public final List<IHeaderHandler> getHeaderHandlers() {
        return CollectionUtils.wrapList(mHeaderHandlers);
    }

    public final int getHttpResponseCode() {
        return mHttpResponseCode;
    }

    public final void setHttpResponseCode(int httpResponseCode) throws ConnectorException {
        mHttpResponseCode = httpResponseCode;
        if (mHttpResponseCode != HttpURLConnection.HTTP_OK) {
            throw new ConnectorException(ConnectorStatus.REQUEST_EXECUTION_FAIL);
        }
    }
}

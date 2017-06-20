package ru.sberbank.mobile.core.network;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.common.base.Objects;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.sberbank.mobile.core.bean.io.Encoding;
import ru.sberbank.mobile.core.bean.io.MimeType;
import ru.sberbank.mobile.core.utils.CollectionUtils;

/**
 * Класс-контейнер для информации о Http-запросе:
 * <ol>
 *     <li>Хост, порт, путь.</li>
 *     <li>Метод HTTP.</li>
 *     <li>Кодировка тела запроса.</li>
 *     <li>Набор пар ключ-значение для GET-запроса.</li>
 *     <li>Контейнер данных для POST/PUT-запросов (IBodySender).</li>
 *     <li>Таймаут на соединение с сервером, количество попыток соединения.</li>
 *     <li>Флаг требования небезопасности соединения.</li>
 * </ol>
 *
 * @author Trikhin P O.
 * @see Method
 * @see IBodySender
 * @see RequestHeaders
 */
public class Request {

    private static final String LOG_TAG = Request.class.getSimpleName();

    public static final int DEFAULT_TIMEOUT = 30000;
    public static final int UNDEFINED_PORT = -1;
    private static final String BOUNDARY = "boundary";

    private final String mHost;
    private int mPort = UNDEFINED_PORT;
    private String mPath;
    private final Map<String, String> mQueryMap = new LinkedHashMap<>();

    private final Method mMethod;
    private final Encoding mEncoding;
    private final Map<String, String> mHeaders = new HashMap<>();
    private IBodySender mBodySender;

    private int mTimeout = DEFAULT_TIMEOUT;
    private boolean mForceUnsecure;

    public Request(@NonNull Method method, @NonNull String host, @NonNull Encoding encoding) {
        this.mMethod = method;
        this.mHost = host;
        this.mEncoding = encoding;
        setupCompulsoryHeaders();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("mHost", mHost)
                .add("mPort", mPort)
                .add("mPath", mPath)
                .add("mQueryMap", mQueryMap)
                .add("mMethod", mMethod)
                .add("mEncoding", mEncoding)
                .add("mHeaders", mHeaders)
                .add("mBodySender", mBodySender)
                .add("mTimeout", mTimeout)
                .add("mForceUnsecure", mForceUnsecure)
                .toString();
    }

    //Getters

    public String getHost() {
        return mHost;
    }

    public int getPort() {
        return mPort;
    }

    public String getPath() {
        return mPath;
    }

    public Map<String, String> getQuery() {
        return CollectionUtils.wrapMap(mQueryMap);
    }

    public Method getMethod() {
        return mMethod;
    }

    public Encoding getEncoding() {
        return mEncoding;
    }

    public Map<String, String> getHeaders() {
        return CollectionUtils.wrapMap(mHeaders);
    }

    @Nullable
    public IBodySender getBodySender() {
        return mBodySender;
    }

    public int getTimeout() {
        return mTimeout;
    }

    public boolean isForceUnsecure() {
        return mForceUnsecure;
    }

    //Setters

    public Request setPort(int port) {
        this.mPort = port;
        return this;
    }

    public Request setPath(@NonNull String path) {
        this.mPath = path;
        return this;
    }

    public Request addQueryKeyValue(@NonNull String name, @NonNull String value) {
        mQueryMap.put(name, value);
        return this;
    }

    public Request addQueryKeyValue(@NonNull String name, @NonNull boolean value) {
        return addQueryKeyValue(name, String.valueOf(value));
    }

    public Request addQueryKeyValue(@NonNull String name, @NonNull int value) {
        return addQueryKeyValue(name, String.valueOf(value));
    }

    public Request addQueryKeyValue(@NonNull String name, @NonNull double value) {
        return addQueryKeyValue(name, String.valueOf(value));
    }

    public Request addQueryKeyValue(@NonNull String name, @NonNull long value) {
        return addQueryKeyValue(name, String.valueOf(value));
    }

    public Request addQueryKeyValue(@NonNull String name, @Nullable List<Object> listOfObjects) {
        String concatObjects = "";
        if (listOfObjects!=null && listOfObjects.size()>0){
            for (Object objectElement : listOfObjects){
                concatObjects = concatObjects + objectElement.toString();
            }
        }
        return addQueryKeyValue(name, concatObjects);
    }

    /**
     * Примечание: вместе с BodySender проставляется из него Content-Type
     * Может быть нулевым
     */
    public Request setBodySender(@Nullable IBodySender bodySender, boolean addContentType) {
        this.mBodySender = bodySender;
        if(bodySender!=null) {
            bodySender.prepareData(mEncoding);
            MimeType mimeType = bodySender.getMimeType();
            if (mimeType == MimeType.MULTI_PART_FORM_DATA) {
                setupMultipart(bodySender.getBoundary(), bodySender.length());
            } else {
                if (addContentType) {
                    setContentType(mimeType, true);
                }
            }
        }
        return this;
    }

    public Request setTimeout(int timeout) {
        this.mTimeout = timeout;
        return this;
    }

    public Request setForceUnsecure(boolean forceUnsecure) {
        this.mForceUnsecure = forceUnsecure;
        return this;
    }

    //public methods

    public Request setAccept(@NonNull MimeType mimeType) {
        addHeader(RequestHeaders.ACCEPT, mimeType.toString());
        return this;
    }

    public Request setAcceptCharset(@NonNull Encoding incomingEncoding) {
        addHeader(RequestHeaders.ACCEPT_CHARSET, incomingEncoding.toString());
        return this;
    }

    public Request setContentType(@NonNull MimeType mimeType, boolean addEncoding) {
        StringBuilder type = new StringBuilder();
        type.append(mimeType.toString());
        if (addEncoding) {
            type.append(";").append("charset=").append(mEncoding.toString());
        }
        addHeader(RequestHeaders.CONTENT_TYPE, type.toString());
        return this;
    }

    public Request setCookie(@NonNull String cookie) {
        addHeader(RequestHeaders.COOKIE, cookie);
        return this;
    }

    /**
     * Устанавливает произвольный заголовок запроса
     * @param name Имя заголовка
     * @param value Значение заголовка
     * @return Этот объект для формирования цепочки вызовов
     */
    public Request setHeader(@NonNull String name, @NonNull String value) {
        // TODO: check header name
        addHeader(name, value);
        return this;
    }

    public String composeQueryAsString() {
        return PostDataBuilder.buildUrlEncodedData(mQueryMap, mEncoding);
    }

    @NonNull
    public String composeURL() {
        StringBuilder url = new StringBuilder(mHost);
        if (mPort != UNDEFINED_PORT) {
            url.append(":").append(mPort);
        }
        if (!TextUtils.isEmpty(mPath)) {
            url.append("/").append(mPath);
        }
        String queryAsString = composeQueryAsString();
        if (!TextUtils.isEmpty(queryAsString)) {
            url.append("?").append(queryAsString);
        }
        return url.toString();
    }

    // Private methods

    private void addHeader(@NonNull String name, @NonNull String value) {
        mHeaders.put(name, value);
    }

    private void setupMultipart(String boundary, long length) {
        StringBuilder type = new StringBuilder(MimeType.MULTI_PART_FORM_DATA.toString());
        type.append(";").append(BOUNDARY).append("=").append(boundary);
        addHeader(RequestHeaders.CONTENT_TYPE, type.toString());
        addHeader(RequestHeaders.CONTENT_LENGTH, String.valueOf(length));
    }

    private void setupCompulsoryHeaders() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2 && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            addHeader(RequestHeaders.CONNECTION, RequestHeaders.CONNECTION_CLOSE);
        }
    }
}

package ru.sberbank.mobile.core.network;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import ru.sberbank.mobile.core.parser.ParserException;
import ru.sberbank.mobile.core.utils.CollectionUtils;

/**
 * Класс-обработчик HTTP-ответа, в своем главном методе {@link #handleResponse(InputStream, Map)}
 * принимает на вход поток с сервера и маппинг заголовков. Поток пропускается через парсер,
 * после чего можно забирать результат через {@link #getResult()}.
 *
 * Дополнительно: сохраняет HTTP-статус ответа ({@link #getHttpResponseCode()}, {@link #setHttpResponseCode(int)}).
 *
 * @author Trikhin P O.
 */
public interface IHttpResponseReceiver<T> {

    void handleResponse(@NonNull InputStream inputStream, @NonNull Map<String, List<String>> headers)
            throws IOException, ParserException;

    T getResult();

    //для StubHttpConnector`а
    void setResult(final T result);

    List<IHeaderHandler> getHeaderHandlers();

    int getHttpResponseCode();

    void setHttpResponseCode(int httpResponseCode) throws ConnectorException;
}

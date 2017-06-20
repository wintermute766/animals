package ru.sberbank.mobile.core.network;

import android.support.annotation.StringRes;

import ru.sberbank.backgroundtaskssample.R;

/**
 * Статус операции с точки зрения клиента: удалось или не удалось взять данные с сервера. Если не удалось,
 * то по какой причине: ошибка соединения, ошибка парсинга.
 */
public enum ConnectorStatus {

    /**
     *
     */
    SUCCESS(0),
    /**
     * ParserException
     */
    PARSER_FAIL(R.string.core_status_parse_fail),
    /**
     * IOException
     */
    NETWORK_UNAVAILABLE(R.string.core_status_network_unavailable),
    /**
     * SocketTimeoutException
     */
    LOW_CONNECTION(R.string.core_status_low_connection),
    /**
     * !HTTP_OK
     */
    REQUEST_EXECUTION_FAIL(R.string.core_status_request_execution_fail),
    /**
     * Не выполнены предусловия для обращения к серверу, например, не удалось получить токен
     */
    SERVICE_UNAVAILABLE(R.string.core_status_service_unavailable);

    @StringRes
    private final int mTextResId;


    ConnectorStatus(@StringRes int textResId) {
        this.mTextResId = textResId;
    }

    @StringRes
    public int getTextResId() {
        return mTextResId;
    }
}

package ru.sberbank.mobile.core.network;

import android.support.annotation.NonNull;

/**
 * Created by Trikhin P O on 07.06.2016.
 */
public interface IHttpConnector {

    <T> T sendAndReceive(@NonNull Request request, @NonNull IHttpResponseReceiver<T> httpResponseReceiver)
            throws ConnectorException;

}

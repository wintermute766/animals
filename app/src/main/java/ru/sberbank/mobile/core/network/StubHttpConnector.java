package ru.sberbank.mobile.core.network;

import android.support.annotation.NonNull;

/**
 * Хелпер только для тестов! Не используйте его на бою!
 *
 * @author QuickNick
 */
public class StubHttpConnector implements IHttpConnector {

    private Request mRequest;
    private Object mResponse;
    private ConnectorStatus mConnectorStatus;

    public StubHttpConnector() {
        mConnectorStatus = ConnectorStatus.SUCCESS;
    }

    @Override
    public <T> T sendAndReceive(@NonNull Request request, @NonNull IHttpResponseReceiver<T> httpResponseReceiver)
            throws ConnectorException {
        mRequest = request;
        if (mConnectorStatus != ConnectorStatus.SUCCESS) {
            throw new ConnectorException(mConnectorStatus);
        }
        httpResponseReceiver.setResult((T) mResponse);
        return (T) mResponse;
    }

    public Request getRequest() {
        return mRequest;
    }

    public void setResponse(Object response) {
        mResponse = response;
    }

    public void setConnectorStatus(ConnectorStatus connectorStatus) {
        mConnectorStatus = connectorStatus;
    }
}

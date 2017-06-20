package ru.sberbank.mobile.core.network;

import android.support.annotation.NonNull;

public class ConnectorException extends Exception {

    private final ConnectorStatus mConnectorStatus;

    public ConnectorException(@NonNull ConnectorStatus connectorStatus) {
        super("(ConnectorStatus: " + connectorStatus + ")");
        this.mConnectorStatus = connectorStatus;
    }

    public ConnectorException(Throwable throwable, @NonNull ConnectorStatus connectorStatus) {
        super("(ConnectorStatus: " + connectorStatus + ")", throwable);
        this.mConnectorStatus = connectorStatus;
    }

    public ConnectorStatus getConnectorStatus() {
        return mConnectorStatus;
    }
}

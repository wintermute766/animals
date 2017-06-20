package ru.sberbank.mobile.core.bean.operation;

import ru.sberbank.mobile.core.network.ConnectorStatus;

/**
 * @author Kudryavtsev Andrey
 * @date 11/10/2016
 */
public class SimpleServerEntity extends ServerEntity {
    @Override
    public boolean isSuccess() {
        return getConnectorStatus() == ConnectorStatus.SUCCESS;
    }
}

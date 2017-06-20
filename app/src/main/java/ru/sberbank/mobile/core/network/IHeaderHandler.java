package ru.sberbank.mobile.core.network;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

public interface IHeaderHandler {

    void handleHeaders(@NonNull Map<String, List<String>> headers);
}

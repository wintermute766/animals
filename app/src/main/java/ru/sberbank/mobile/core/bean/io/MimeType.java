package ru.sberbank.mobile.core.bean.io;

/**
 * Created by QuickNick on 09/06/16.
 */
public enum MimeType {

    APPLICATION_JSON("application/json"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    IMAGE_JPEG("image/jpeg"),
    MULTI_PART_FORM_DATA("multipart/form-data");

    private final String mName;

    MimeType(String name) {
        mName = name;
    }

    public final String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return mName;
    }
}

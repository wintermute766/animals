package ru.sberbank.mobile.core.bean.io;

/**
 * Created by Trikhin P O on 07.06.2016.
 */
public enum Encoding {

    WINDOWS_1251("windows-1251"),
    UTF_8("UTF-8");

    private final String mName;

    Encoding(String name) {
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

package ru.sberbank.mobile.core.bean.io;

import java.io.File;

/**
 * Created by QuickNick on 09/06/16.
 */
public class FileInfo {

    private final String mKey;
    private final String mName;
    private final MimeType mMimeType;
    private final File mFile;

    public FileInfo(String key, String name, MimeType mimeType, File file) {
        mKey = key;
        mName = name;
        mMimeType = mimeType;
        mFile = file;
    }

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

    public MimeType getMimeType() {
        return mMimeType;
    }

    public File getFile() {
        return mFile;
    }
}

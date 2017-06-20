package ru.sberbank.mobile.core.utils;

import android.content.Context;
import android.support.annotation.RawRes;

import java.io.IOException;
import java.io.InputStream;

import ru.sberbank.mobile.core.parser.IParser;
import ru.sberbank.mobile.core.parser.ParserException;

/**
 * @author QuickNick.
 */
public class TestUtils {

    public static <T> T parseFromRawResource(Context context, @RawRes int resId, IParser parser, Class<T> clazz)
            throws IOException, ParserException {
        InputStream source = null;
        T result = null;
        try {
            source = context.getResources().openRawResource(resId);
            result = parser.parse(source, clazz);
        } finally {
            try {
                source.close();
            } catch (IOException e) {
            }
        }
        return result;
    }
}

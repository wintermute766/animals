package ru.sberbank.mobile.core.utils;

import java.util.Locale;

/**
 * Created by QuickNick on 09/06/16.
 */
public class LocaleUtils {

    private static final Locale RUSSIAN_LOCALE = new Locale("RU");
    private static final Locale ENGLISH_LOCALE = Locale.ENGLISH;

    /**
     * Метод, возвращающий "текущую" локаль. На сегодняшний день (09.06.2016) это только русская локаль.
     * Когда нам просигналят, что внедряем мультиязычность, подберем актуальную локаль.
     * @return "текущая" локаль
     */
    public static Locale getCurrentLocale() {
        return RUSSIAN_LOCALE;
    }

    public static Locale getRussianLocale() {
        return RUSSIAN_LOCALE;
    }

    public static Locale getEnglishLocale() {
        return ENGLISH_LOCALE;
    }
}
package ru.sberbank.mobile.core.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author Leonid Emelaynov on 09.01.2017.
 */

public class DecimalStringUtils {
    private static final String NEED_TRIM_REGEX = "^0+$|^0*[%1$s]0*$";
    private static final String ZERO_TRIM_REGEX = "^0*([1-9]*)0*$";
    private static final String ZERO_TRIM_WITH_SEPARATOR_REGEX = "^0*([1-9]*)0*[%1$s]+(0?)0*([1-9]*)0*$";
    private static final String GROUPING_REGEX = "[^%1$s]";
    private static final String WRONG_SYMBOLS_REGEX = "[\\D&&[^-%1$s]]";
    private static final String DOUBLE_MINUS_REGEX = "([^-]+)-";
    private static final String DOUBLE_SEPARATOR_REGEX = "([%1$s])(.*)([%1$s])";

    /**
     * Если строковая величина обрамлена нулями, метод вернёт true
     *
     * @param value строковое представление числа
     * @return флаг, указывающий, обрамлена ли строка нулями
     */
    public static boolean isNeedTrimValue(@Nullable String value, Locale locale) {
        char separator = getSeparator(locale);

        return value != null
                && (TextUtils.isEmpty(value) || value.matches(String.format(NEED_TRIM_REGEX, separator)));
    }

    /**
     * Метод для получения "чистой" строки без пробелов, повторяющихся символов разделителя, +, -, /
     *
     * @param value "грязная" строка с лишними символами
     * @return "чистая" строка без лишних символов
     */
    public static String getPureValue(String value, DecimalFormat decimalFormat) {
        String pureValue = value;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        String separator = String.valueOf(symbols.getDecimalSeparator());

        if (!TextUtils.isEmpty(pureValue)) {
            pureValue = pureValue
                    .replaceAll(String.format(WRONG_SYMBOLS_REGEX, separator), "")
                    .replaceAll(DOUBLE_MINUS_REGEX, "$1")
                    .replaceAll(String.format(DOUBLE_SEPARATOR_REGEX, separator), "$2$3");
        }

        return pureValue;
    }

    /**
     * Метод для получения строки без обрамляющих нулей
     *
     * @param value строка с нулями
     * @return строку без нулей
     */
    public static String getTrimZeroValue(@NonNull String value, Locale locale) {
        char separator = getSeparator(locale);

        return value.replaceAll(String.format(ZERO_TRIM_WITH_SEPARATOR_REGEX, separator), "$1" + separator + "$2$3")
                .replaceAll(ZERO_TRIM_REGEX, "$1");
    }

    /**
     * Получение количества знаков после запятой или точки для текствого представления числа
     *
     * @param value     текствое представление числа
     * @param trimZeros нe читывать нули после запятой
     * @return количество знаков после запятой
     */
    public static int getScale(@Nullable String value, boolean trimZeros, char separator) {
        int round = 0;

        if (!TextUtils.isEmpty(value)) {
            int pos = value.lastIndexOf(separator);

            if (pos > -1 && pos < value.length()) {
                if (trimZeros) {
                    String reminder = value.substring(pos + 1);

                    for (round = reminder.length(); round > 0; round--) {
                        if (reminder.charAt(round - 1) != '0') {
                            break;
                        }
                    }
                } else {
                    round = Math.max(value.length() - pos - 1, 0);
                }
            }
        }

        return round;
    }

    /**
     * Метод для получения символа разделителя дробной части в указанной локали
     *
     * @param locale Локаль
     * @return Символ разделителя
     */
    public static char getSeparator(Locale locale) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(locale);
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();

        return symbols.getDecimalSeparator();
    }

    /**
     * Метод для получения символа разделителя разрядов в указанной локали
     *
     * @param locale Локаль
     * @return Символ разделителя
     */
    public static char getGroupingSeparator(Locale locale) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(locale);
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();

        return symbols.getGroupingSeparator();
    }

    /**
     * Получение количества знаков после разделителя для числа в формате {@link BigDecimal}
     *
     * @param value число
     * @return количество знаков после разделителя
     */
    public static int getScale(@Nullable BigDecimal value) {
        int round = 0;

        if (value != null) {
            round = value.stripTrailingZeros().scale();
        }

        return round;
    }

    /**
     * Получение количества разрядов в вещественной части числа
     *
     * @param value число
     * @return количество разрядов
     */
    public static int getDecimalDigits(@Nullable BigDecimal value) {
        int digits = 0;

        if (value != null) {
            BigDecimal stripValue = value.stripTrailingZeros();
            digits = stripValue.precision() - stripValue.scale();
        }

        return Math.max(0, digits);
    }

    /**
     * Разница в количестве пробелов между двумя строками
     *
     * @param newValue новая строка
     * @param oldValue старая строка
     * @return разница в колчестве пробелов между новой и старой строками
     */
    public static int getSpacesDiffCount(@Nullable String newValue, @Nullable String oldValue, Locale locale) {
        String grouping = Pattern.quote(String.valueOf(getGroupingSeparator(locale)));
        String groupingRegex = String.format(GROUPING_REGEX, grouping);

        return (TextUtils.isEmpty(newValue)
                ? 0
                : newValue.replaceAll(groupingRegex, "").length())
                - (TextUtils.isEmpty(oldValue)
                ? 0
                : oldValue.replaceAll(groupingRegex, "").length());
    }
}
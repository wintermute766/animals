package ru.sberbank.mobile.core.format;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import ru.sberbank.mobile.core.utils.DecimalStringUtils;
import ru.sberbank.mobile.core.utils.LocaleUtils;

/**
 * Класс-форматтер для числовых данных. На данный момент работа ведется только с BigDecimal,
 * добавление новых типов — Welcome.
 */
public class DecimalFormatter {

    /**
     * Число знаков после запятой по умолчанию: {@value}
     */
    public static final int DEFAULT_SCALE = 2;

    /**
     * Свойства форматирования по умолчанию: точность {@link #DEFAULT_SCALE}, отсечение незначащих
     * нулей, сохранение знака, группировка разрядов.
     */
    public static final FormatProperties DEFAULT_PROPERTIES =
            new FormatProperties(DEFAULT_SCALE, true, false, true);
    /**
     * Свойства форматирования модуля числа: точность {@link #DEFAULT_SCALE}, отсечение незначащих
     * нулей, потеря знака, группировка разрядов.
     */
    public static final FormatProperties DEFAULT_ABS_PROPERTIES =
            new FormatProperties(DEFAULT_SCALE, true, true, true);

    /**
     * Свойства форматирования по умолчанию: точность 0, отсечение незначащих
     * нулей, сохранение знака, группировка разрядов.
     */
    public static final FormatProperties DEFAULT_INTEGER_PROPERTIES =
            new FormatProperties(0, true, false, true);
    /**
     * Свойства форматирования модуля числа: точность 0, отсечение незначащих
     * нулей, потеря знака, группировка разрядов.
     */
    public static final FormatProperties DEFAULT_ABS_INTEGER_PROPERTIES =
            new FormatProperties(0, true, true, true);
    /**
     * Свойства форматирования без группировки: точность {@link #DEFAULT_SCALE}, отсечение незначащих
     * нулей, сохранение знака, нет группировки.
     */
    public static final FormatProperties NO_GROUPING_PROPERTIES =
            new FormatProperties(DEFAULT_SCALE, true, false, false);

    /**
     * Парсит BigDecimal из строки с указанной локалью.
     *
     * @param source Строка-источник
     * @param locale Локаль
     * @return Распарсенный BigDecimal или {@code null}
     */
    @Nullable
    public static BigDecimal parseBigDecimal(@NonNull String source, @NonNull Locale locale) {
        BigDecimal decimal = null;
        try {
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(locale);
            df.setParseBigDecimal(true);
            decimal = (BigDecimal) df.parse(DecimalStringUtils.getPureValue(source, df));
        } catch (ParseException e) {
        } finally {
        }
        return decimal;
    }

    /**
     * Парсит BigDecimal с русской локалью.
     *
     * @param source Строка-источник
     * @return Распарсенный {@code BigDecimal} или {@code null}
     */
    @Nullable
    public static BigDecimal parseBigDecimal(@NonNull String source) {
        Locale locale = LocaleUtils.getCurrentLocale();
        return parseBigDecimal(source, locale);
    }

    /**
     * Форматирует {@code BigDecimal} с указанными параметрами.
     *
     * @param decimal    Число для форматирования
     * @param locale     Локаль для форматирования
     * @param properties Набор параметров для форматирования; не может быть {@code null}, следует
     *                   явно передавать {@link #DEFAULT_PROPERTIES} для случая по умолчанию.
     * @return Отформатированная строка
     */
    public static String format(@NonNull BigDecimal decimal, @NonNull Locale locale, @NonNull FormatProperties properties) {
        BigDecimal decimalCopy = decimal.setScale(properties.scale, RoundingMode.HALF_DOWN);
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(locale);
        if (properties.tryCleanFractionalPart) {
            BigDecimal remainder = decimal.abs().remainder(BigDecimal.ONE);
            if (remainder.compareTo(BigDecimal.ZERO) == 0) {
                decimalCopy = decimalCopy.setScale(0, RoundingMode.HALF_DOWN);
            } else {
                df.setMinimumFractionDigits(properties.scale);
            }
        } else {
            df.setMinimumFractionDigits(properties.scale);
        }
        if (properties.applyAbs) {
            decimalCopy = decimalCopy.abs();
        }
        df.setGroupingUsed(properties.groupingUsed);
        String result = df.format(decimalCopy);
        return result;
    }

    /**
     * Форматирование числа в указанной локали с настройками по умолчанию.
     *
     * @param decimal Число
     * @param locale  Локаль
     * @return Отформатированная строка
     * @see #format(BigDecimal, Locale, FormatProperties)
     */
    public static String format(@NonNull BigDecimal decimal, @NonNull Locale locale) {
        return format(decimal, locale, DEFAULT_PROPERTIES);
    }

    /**
     * Форматирование числа в текущей локали с указанными параметрами форматирования.
     *
     * @param decimal    Число
     * @param properties Свойства форматирования
     * @return Отформатированная строка
     * @see #format(BigDecimal, Locale, FormatProperties)
     */
    public static String format(@NonNull BigDecimal decimal, @NonNull FormatProperties properties) {
        Locale locale = LocaleUtils.getCurrentLocale();
        return format(decimal, locale, properties);
    }

    /**
     * Форматирование числа со всеми параметрами по умолчанию
     *
     * @param decimal Число
     * @return Отформатированная строка
     * @see #format(BigDecimal, Locale, FormatProperties)
     */
    public static String format(@NonNull BigDecimal decimal) {
        return format(decimal, DEFAULT_PROPERTIES);
    }

    /**
     * Форматирование числа по модулю ({@link #DEFAULT_ABS_PROPERTIES}) со всеми параметрами по умолчанию
     *
     * @param decimal Число
     * @return Отформатированная строка
     * @see #format(BigDecimal, Locale, FormatProperties)
     */
    public static String formatAbs(@NonNull BigDecimal decimal) {
        return format(decimal, DEFAULT_ABS_PROPERTIES);
    }

    /**
     * Настройки форматирования чисел, используемые методами
     * {@link #format(BigDecimal, Locale, FormatProperties) format} в родительском классе.
     */
    public static class FormatProperties {

        private final int scale;
        private final boolean tryCleanFractionalPart;
        private final boolean applyAbs;
        private final boolean groupingUsed;

        /**
         * Инициализация всех параметров форматирования.
         *
         * @param scale                  Число десятичных знаков в формате числа
         * @param tryCleanFractionalPart Следует ли отсекать нулевую десятичную часть (т.е. форматировать,
         *                               например, круглое число {@code 123} просто как {@code 123};
         *                               в противном случае, например, результат
         *                               получится {@code 123,00}.
         * @param applyAbs               Взять ли абсолютное значение числа, т.е. модуль, {@code Math.abs()}.
         * @param groupingUsed           Использовать ли группировку разрядов ({@code 12 345} или {@code 12345})
         */
        public FormatProperties(int scale, boolean tryCleanFractionalPart, boolean applyAbs, boolean groupingUsed) {
            this.scale = scale;
            this.tryCleanFractionalPart = tryCleanFractionalPart;
            this.applyAbs = applyAbs;
            this.groupingUsed = groupingUsed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof FormatProperties)) {
                return false;
            }
            FormatProperties that = (FormatProperties) o;
            return scale == that.scale &&
                    tryCleanFractionalPart == that.tryCleanFractionalPart &&
                    applyAbs == that.applyAbs &&
                    groupingUsed == that.groupingUsed;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(scale, tryCleanFractionalPart, applyAbs, groupingUsed);
        }
    }
}
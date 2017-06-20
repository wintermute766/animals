package ru.sberbank.mobile.core.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.sberbank.mobile.core.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

public class CollectionUtils {

    public static <T> List<T> wrapList(@Nullable List<T> list) {
        List<T> resultList = null;
        if (list != null) {
            resultList = new ArrayList<>(list);
        }
        return resultList;
    }

    public static <T> Set<T> wrapSet(@Nullable Set<T> set) {
        Set<T> resultSet = null;
        if (set != null) {
            resultSet = new HashSet<>(set);
        }
        return resultSet;
    }

    public static <K, V> Map<K, V> wrapMap(@Nullable Map<K, V> map) {
        Map<K, V> resultMap = null;
        if (map != null) {
            resultMap = new HashMap<>(map);
        }
        return resultMap;
    }

    public static <T> void clearAndFill(@Nullable List<T> source, @NonNull List<T> target) {
        target.clear();
        if (source != null) {
            target.addAll(source);
        }
    }

    public static <T> List<T> mergeLists(Class<T> clazz, List<? extends T>... lists) {
        List<T> result = new ArrayList<>();
        for (List<? extends T> source : lists) {
            result.addAll(source);
        }
        return result;
    }

    /**
     * Возвращает пустой список, если входящий параметр null
     * Может применяться, напрмиер, в контексте оператора for:
     * List<SomeType> someTypeList = null;
     * for(SomeType item : emptyIfNull(someTypeList)){
     *     // do something...
     * }
     * для предотвращения NPE
     * @param iterable any nullable iterable
     * @return входящий параметр, если он не null, или пустой список {@link java.util.Collections.EMPTY_LIST}
     */
    @NonNull
    public static <T> Iterable<T> emptyIfNull(@Nullable final Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = null;
        if (list == null || list.isEmpty()) {
            result = Collections.emptyList();
        } else {
            checkNotNull(predicate);
            result = new ArrayList<T>();
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < list.size(); i++) {
                T item = list.get(i);
                if (predicate.apply(item)) {
                    result.add(item);
                }
            }
        }
        return result;
    }
}

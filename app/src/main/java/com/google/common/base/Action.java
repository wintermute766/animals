package com.google.common.base;

public interface Action<T> {

    void apply(T arg);
}

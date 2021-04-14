package com.go.basetool.threadstatus;

public interface DataBinder<T> {
    void put(T t);

    T get();

    void remove();
}
package com.go.basetool.threadstatus;

public class ThreadLocalDataBinder<T> implements DataBinder<T> {
    private final ThreadLocal<T> threadLocal = new ThreadLocal<>();

    @Override
    public void put(T t) {
        threadLocal.set(t);
    }

    @Override
    public T get() {
        return threadLocal.get();
    }

    @Override
    public void remove() {
        threadLocal.remove();
    }
}
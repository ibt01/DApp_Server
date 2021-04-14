package com.go.basetool.threadstatus;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class DataBinderManager {

    private final static ConcurrentHashMap<String, DataBinder> BINDER_MAP = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> DataBinder<T> getDataBinder(final String bindType) {
        DataBinder dataBinder = BINDER_MAP.get(bindType);
        if (dataBinder == null) {
            DataBinder bind = new ThreadLocalDataBinder();
            dataBinder = BINDER_MAP.putIfAbsent(bindType, bind);
            if (dataBinder == null) {
                dataBinder = bind;
            }
        }
        return dataBinder;
    }
//一次请求----10个接口，用户是谁？
//一个线程-第一个入口
}

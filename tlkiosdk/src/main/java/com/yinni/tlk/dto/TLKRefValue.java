package com.yinni.tlk.dto;

public class TLKRefValue<T> {
    public T data;

    public TLKRefValue(){
        data = null;
    }

    public TLKRefValue(T initialVal ){
        data = initialVal;
    }
}
